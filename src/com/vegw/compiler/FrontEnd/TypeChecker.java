package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.AST.Expr.*;
import com.vegw.compiler.AST.Stmt.*;
import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.ParameterEntity;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.Type.ArrayType;
import com.vegw.compiler.Type.ClassType;
import com.vegw.compiler.Type.Type;
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

    public void check(ASTNode ast) {
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
        switch (node.operator()) {
            case PREM: case PREP:
            case POSP: case POSM:
            case POS: case NEG:
            case BITN: break;
            case LOGN: expect = Type.BOOL; break;
            default:
                errorHandler.error(node,  "Unknown unary operation");

        }
        expect.isSameType(node.type());
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
            case SUB: case MUL: case DIV: case MOD:
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
                node.setType(left);
                ((VariableNode) node.left()).entity().setValue(node.right());
            }
            default:
                errorHandler.error(node, "Unknown Binary Operator");
        }
        return null;
    }

    @Override
    public Void visit(FuncallNode node) {
        List<ParameterEntity> params = node.functionType().entity().params();
        List<ExprNode> args = node.params();
        if (params.size() != args.size())
            errorHandler.error(node, "The number of parameters differs with arguments given");
        else {
            for (Iterator pitr = params.iterator(), aitr = args.iterator(); pitr.hasNext();) {
                Type paramType = ((ParameterEntity)pitr.next()).type();
                Type argType = ((ExprNode)aitr.next()).type();
                if (!argType.isSameType(paramType)) {
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
        Type baseType = node.baseExpr().type();
        if (baseType == Type.VOID || baseType == Type.NULL) {
            errorHandler.error(node, "Illegal array base type: " + baseType.toString());
        }
        Type base = node.base().type();
        Type index = node.index().type();
        if (!(base instanceof ArrayType)) {
            errorHandler.error(node, "Indexing a nonarray object");
            node.setType(baseType);
            return null;
        }
        else if (!(index == Type.INT))
            errorHandler.error(node, "Noninteger index");
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
        if (!(type instanceof ClassType)) {
            errorHandler.error(node, "Member access applied to a nonclass object");
            return null;
        }
        VariableEntity ent = (VariableEntity)((ClassType) type).entity().scope().getCurrentScope(node.member());
        if (ent == null) {
            errorHandler.error(node, "Required member missing");
            return null;
        }
        node.setType(ent.type());
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
    public Void visit(BreakNode node) {
        if (depth == 0)
            errorHandler.error(node, "Break without loop");
        depth -= 1;
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
        visit(node.init());
        visit(node.cond());
        visit(node.step());
        depth += 1;
        visit(node.body());
        depth -= 1;
        return null;
    }

    @Override
    public Void visit(IfNode node) {
        visit(node.thenBody());
        visit(node.elseBody());
        if (!(node.cond().type() == Type.BOOL))
            errorHandler.error(node, "Condition expression for if returns a non bool value");
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
        depth += 1;
        visit(node.body());
        depth -= 1;
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
        if (entity.returnType() == null && entity.isConstructor()) {
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
        if (value != null) {
            visit(value);
            if (!(entity.type().isSameType(value.type()))){
                errorHandler.error(node, "Initialization conflicts with variable type");
            }
            if (value.type() == Type.VOID) {
                errorHandler.error(node, "Illegal type void for variable");
            }
        }
        return null;
    }
}
