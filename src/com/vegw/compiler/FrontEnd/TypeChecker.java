package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.AST.Expr.*;
import com.vegw.compiler.AST.Expr.Literal.IntegerLiteralNode;
import com.vegw.compiler.AST.Stmt.*;
import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.Entity.*;
import com.vegw.compiler.Exception.SemanticException;
import com.vegw.compiler.Type.*;
import com.vegw.compiler.Utils.ErrorHandler;

import java.util.Iterator;
import java.util.List;

public class TypeChecker extends Visitor {
    private ErrorHandler errorHandler;
    private int depth;
    private FunctionEntity curFunc;
    public TypeChecker(ErrorHandler h) {
        depth = 0;
        errorHandler = h;
    }

    public void check(ASTNode ast) throws SemanticException {
        try {
            System.out.println("Hello!");
            Entity entity = ast.scope.get("main");
            if (!(entity instanceof FunctionEntity))
                throw new SemanticException("Variable and class cannot be named as 'main'");
            else if (((FunctionEntity) entity).returnType() != Type.INT)
                throw new SemanticException("Main function return noninteger result");
        } catch (SemanticException se) {
            errorHandler.error(ast, "Main function missing");
            throw(se);
        }
        visit(ast.defs);
    }


    public Void visit(List<DefinitionNode> defs) {
        for (DefinitionNode def : defs) {
            visit(def);
        }
        return null;
    }

    @Override
    public Void visit(UnaryOpNode node) {
        super.visit(node);
        node.setType(node.expr().type());
        Type expect = Type.INT;
        boolean checkEditable = false;
        switch (node.operator()) {
            case POSP: case POSM: checkEditable = true;
            case PREM: case PREP:
            case POS: case NEG:
            case BITN: break;
            case LOGN: expect = Type.BOOL; break;
            default:
                errorHandler.error(node,  "Unknown unary operation");
        }
        if (!expect.isSameType(node.type())) {
            errorHandler.error(node,  "Unary operation " + node.operator().name() +
                    " cannot be applied to type" + node.expr().type().toString());
            return null;
        }
        if (checkEditable) {
            if (!node.expr().isAssignable())
                errorHandler.error(node,  "Expression cannot taken as lvalue");
        }
        return null;
    }

    @Override
    public Void visit(BinaryOpNode node) {
        super.visit(node);
        Type left = node.left().type();
        Type right = node.right().type();

        switch (node.operator()) {
            case ADD: {
                if (!left.isSameType(right)) {
                    errorHandler.error(node, "Left and right expression type differs");
                    break;
                }
                if (left.isSameType(Type.INT) || left.isSameType(Type.STRING)) {
                    node.setType(left);

                }
                else
                    errorHandler.error(node, "Binary operator " + node.operator().name() +
                            " cannot be applied to type:" + left.toString());

                break;
            }
            case LT: case GT:
            case LE: case GE: {
                if (!left.isSameType(right)) {
                    errorHandler.error(node, "Left and right expression type differs");
                    break;
                }
                if (left.isSameType(Type.INT) || left.isSameType(Type.STRING)) {
                    node.setType(Type.BOOL);
                }
                else
                    errorHandler.error(node, "Binary operator " + node.operator().name() +
                            " cannot be applied to type:" + left.toString());
                break;
            }
            case SUB: case MUL: case MOD:
            case LS: case RS:
            case BIT_OR: case BIT_AND: case XOR:{
                if (left == Type.INT && right == Type.INT) {
                    node.setType(left);
                }
                else
                    errorHandler.error(node, "Binary operator " + node.operator().name() +
                            " cannot be applied to type:" + left.toString());
                break;
            }
            case DIV: {
                if (left == Type.INT && right == Type.INT) {
                    node.setType(left);
                    if (node.right() instanceof IntegerLiteralNode) {
                        if (((IntegerLiteralNode) node.right()).value() == 0)
                            errorHandler.error(node, "Binary operator " + node.operator().name() +
                                    " cannot be applied when right expression is zero");
                    }
                }
                else
                    errorHandler.error(node, "Binary operator " + node.operator().name() +
                            " cannot be applied to type:" + left.toString());
                break;
            }
            case LOG_OR: case LOG_AND: {
                if (left == Type.BOOL && right == Type.BOOL) {
                    node.setType(left);
                }
                else
                    errorHandler.error(node, "Binary operator " + node.operator().name() +
                            " cannot be applied to type:" + left.toString());
                break;
            }
            case EQ: case NE: {
                node.setType(Type.BOOL);
                if (!left.isConvertable(right)) {
                    errorHandler.error(node, "Left and right expression type cannot be compared");
                    break;
                }
                break;
            }
            case ASSIGN: {
                node.setType(left);
                if (!node.left().isAssignable() || left == Type.NULL || left == Type.VOID) {
                    errorHandler.error(node, "Left expression cannot be applied as a lvalue");
                    break;
                }
                if (!left.isConvertable(right)) {
                    errorHandler.error(node, "Left expression cannot be assigned for type dismatching");
                }
                break;
            }
            default:
                errorHandler.error(node, "Unknown Binary Operator");
        }
        return null;
    }

    @Override
    public Void visit(FuncallNode node) {
        super.visit(node);
        List<ParameterEntity> params = node.functionType().entity().params();
        List<ExprNode> args = node.params();

        boolean isMemberFunction = params.size() > 0? params.get(0).name().equals("this"): false;
        int paramOffset = isMemberFunction? 1 : 0;
        if (params.size() != args.size() + paramOffset)
            errorHandler.error(node, "The number of parameters differs with arguments given");
        else {
            Iterator pitr = params.iterator();
            Iterator aitr = args.iterator();
            if (isMemberFunction) pitr.next();
            for (; pitr.hasNext();) {
                Type paramType = ((ParameterEntity)pitr.next()).type();
                Type argType = ((ExprNode)aitr.next()).type();
                if (!argType.isConvertable(paramType)) {
                    errorHandler.error(node, "The argument differs with required type");
                }
            }
        }
        return null;
    }


    @Override
    public Void visit(ArefNode node) {
        visit(node.base());
        visit(node.index());
        Type base = node.base().type();
        Type index = node.index().type();
        if (!(base instanceof ArrayType)) {
            errorHandler.error(node, "Indexing a nonarray object");
            node.setType(base);
            return null;
        }
        else if (!(index == Type.INT))
            errorHandler.error(node, "Noninteger index");
        Type baseType = ((ArrayType)node.baseExpr().type()).baseType();
        int newDemension = ((ArrayType) base).demension() - 1;
        if (newDemension > 0)
            node.setType(new ArrayType(baseType, newDemension));
        else
            node.setType(baseType);
        return null;
    }

    @Override
    public Void visit(MemberNode node) {
        super.visit(node.field());
        Type type = node.field().type();
        if (!isMemberAccessible(type)) {
            errorHandler.error(node, "Member access applied to a nonclass object");
            return null;
        }
        Scope scope;
        String prefix = "";
        if (type instanceof ClassType) { scope = ((ClassType) type).entity().scope(); }
        else if (type instanceof ArrayType) { scope = ((ArrayType) type).entity().scope(); }
        else { scope = ((StringType) type).entity().scope(); }


            Entity ent = scope.getCurrentScope(node.member());
        if (ent == null) {
            errorHandler.error(node, "Required member missing");
            return null;
        }

        node.setEntity(ent);

        return null;
    }


    @Override
    public Void visit(CreatorNode node) {
        for (ExprNode expr : node.dimensionExpr()) {
            visit(expr);
            if (expr.type() != Type.INT)
                errorHandler.error(expr,"Array dimension accepts integer only");
        }
        if (node.type() == Type.NULL || node.type() == Type.VOID)
            errorHandler.error(node, "Create array type with illegal type");
        if (node.dimensionExpr().size() == 0) {
            if (node.type() == Type.BOOL || node.type() == Type.STRING
                    || node.type() == Type.INT) {
                errorHandler.error(node, "Create primitive type with new operation");
            }
        }
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        for (StmtNode stmt : node.stmts()) {
            visit(stmt);
            if (stmt instanceof BreakNode || stmt instanceof ContinueNode)
                break;
        }
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        if (depth == 0)
            errorHandler.error(node, "Break without loop");
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        if (depth == 0)
            errorHandler.error(node, "Continue without loop");
        return null;
    }

    @Override
    public Void visit(ForNode node) {
        if (node.init() != null)
            visit(node.init());
        if (node.cond() != null) {
            visit(node.cond());
            if (node.cond().type() != Type.BOOL)
                errorHandler.error(node, "If condition expression returns non boolean result");
        }
        if (node.step() != null)
            visit(node.step());
        if (node.body() != null) {
            depth += 1;
            visit(node.body());
            depth -= 1;
        }
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        if (node.thenBody() != null)
            visit(node.thenBody());
        if (node.elseBody() != null)
            visit(node.elseBody());
        visit(node.cond());
        if (!(node.cond().type() == Type.BOOL))
            errorHandler.error(node, "Condition expression for if returns a non boolean value");
        return null;
    }

    @Override
    public Void visit(ReturnNode node) {
        if (curFunc == null) {
            errorHandler.error(node, "Return outside any function");
            return null;
        }
        ExprNode expr = node.expr();
        if (curFunc.returnType() == null || curFunc.returnType() == Type.VOID) {
            if (expr != null) {
                errorHandler.error(node, "Function with void result has return stmt");
            }
        }
        else if (expr != null) {
            visit(expr);
            if (!curFunc.returnType().isConvertable(expr.type())) {
                errorHandler.error(node, "Function result imcompatible with return value");
            }
        }
        else errorHandler.error(node, "Missing return value");
        return null;
    }

    @Override
    public Void visit(WhileNode node) {
        ExprNode cond = node.cond();
        visit(cond);
        if (cond.type() != Type.BOOL) {
            errorHandler.error(node, "Condition expression returns non bool value");
        }
        if (node.body() != null) {
            depth += 1;
            visit(node.body());
            depth -= 1;
        }
        return null;
    }

    @Override
    public Void visit(FunctionDefNode node) {
        FunctionEntity entity = node.entity();
        curFunc = entity;
        if (entity.returnType() == Type.NULL) {
            errorHandler.error(node, "Illegal function return type: null");
            return null;
        }
        if (entity.returnType() == null && !entity.isConstructor()) {
            errorHandler.error(node, "Nonconstructor function with null return type");
            return null;
        }
        visit(entity.body());
        for (ParameterEntity param : entity.params()) {
            if (param.type() == Type.NULL || param.type() == Type.VOID)
                errorHandler.error(node, "Illegal type " + param.type().toString() +
                                                  " for parameter");
        }
        curFunc = null;
        return null;
    }

    @Override
    public Void visit(VariableDefNode node) {
        VariableEntity entity = node.entity();
        ExprNode value = entity.value();
        Type type = entity.type();
        if (type == Type.VOID || type == Type.NULL) {
            errorHandler.error(node, "Illegal variable declaration with type '" + type.toString() + "'");
        }
        if (type instanceof ArrayType) {
            Type baseType = ((ArrayType)type).baseType();
            if (baseType == Type.VOID || baseType == Type.NULL) {
                errorHandler.error(node, "Illegal array base type: " + baseType.toString());
            }
        }
        if (value != null) {
            visit(value);
            if (!(type.isConvertable(value.type()))){
                errorHandler.error(node, "Initialization conflicts with variable type");
            }
            if (value.type() == Type.VOID) {
                errorHandler.error(node, "Illegal type void for variable");
            }
        }
        return null;
    }

    private boolean isMemberAccessible(Type type) {
        return (type == Type.STRING || type instanceof ClassType || type instanceof ArrayType);
    }
}
