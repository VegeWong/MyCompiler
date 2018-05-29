package com.vegw.compiler.BackEnd;

import com.vegw.compiler.AST.ASTNode;
import com.vegw.compiler.AST.Expr.*;
import com.vegw.compiler.AST.Expr.Literal.BoolLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.IntegerLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.NullLiteralNode;
import com.vegw.compiler.AST.Expr.Literal.StringLiteralNode;
import com.vegw.compiler.AST.Stmt.*;
import com.vegw.compiler.AST.Stmt.Def.ClassDefNode;
import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.Entity.*;
import com.vegw.compiler.Exception.InternException;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.FrontEnd.LocalScope;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.IR.Tree.*;
import com.vegw.compiler.Type.ArrayType;
import com.vegw.compiler.Type.ClassType;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.BuiltinFunction;
import com.vegw.compiler.Utils.ErrorHandler;
import com.vegw.compiler.Utils.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class IRGenerator implements ASTVisitor<Void,Expr> {
    private final static Imme ZERO = new Imme(0);
    private final static Imme ONE = new Imme(1);

    private List<IRInstruction> stmts;
    protected ASTNode ast;
    private FunctionEntity curFunc;
    private Stack<Scope> stack;
    private Scope curScope;

    // Target of continue and break in loop
    private Label continueTarget = null;
    private Label breakTarget = null;

    // Multilayer arefNode depth
    private int arefDepth = 0;



    // ======== Function =============

    private ErrorHandler errorHandler = new ErrorHandler("IRGenerator");

    // Create an virtual variable for self-inc or self-dec
    private int globalTmpCnt = 0;
    private Var createIntTmp() {
        String name = "tmp" + String.valueOf(globalTmpCnt++);
        ExprNode value = null;
        LocalScope newScope = new LocalScope(curScope);
        VariableEntity entity = new VariableEntity(new Location(0,0), name, Type.INT);
        entity.setScope(newScope);
        Var tmp = new Var(entity);
        return tmp;
    }

    private void processAssign(Expr dst, Expr src) throws InternException {
        Expr dstAddr;

        if (dst instanceof Var)
            dstAddr = new Addr(((Var) dst).entity);
        else if (dst instanceof Mem)
            dstAddr = ((Mem) dst).addr;
        else
            throw new InternException("Invalid assign object");

        stmts.add(new Assign(dstAddr, src));
    }

    public IRGenerator(ASTNode ast) {
        this.stmts = new LinkedList<IRInstruction>();
        this.ast = ast;
        stack.push(ast.scope);
        curScope = stack.peek();
    }

    public void generate() {

        Map<String, Entity> map = ast.scope.entities();
        for (String key : map.keySet()) {
            Entity entity = map.get(key);
            if (entity instanceof ClassEntity)
                ((ClassEntity) entity).setOffset();
        }

        String FUNC_PREFIX = "__FUNC__";
        String CLASS_PREFIX = "__CLASS__";
        String FIX;
        for (DefinitionNode def : ast.defs) {
            if (def instanceof VariableDefNode) continue;
            if (def instanceof FunctionDefNode) {
                FunctionEntity e = ((FunctionDefNode) def).entity();
                curFunc = e;
                e.rename(FUNC_PREFIX + e.name());
                compileFunction(e);
            }
            else {
                ClassEntity e = ((ClassDefNode) def).entity();
                String name = CLASS_PREFIX + e.name();
                e.rename(name);
                for (FunctionDefNode func : e.funcs()) {
                    FIX = name + "." + FUNC_PREFIX;
                    FunctionEntity fe = ((FunctionDefNode) func).entity();
                    curFunc = fe;
                    fe.rename(FIX + fe.name());
                    compileFunction(fe);
                }
            }
        }




    }

    private void compileFunction(FunctionEntity e) {
        Label begin = new Label(e.internalName() + ".__begin__");
        Label end = new Label(e.internalName() + ".__end__");
        e.begin = begin;
        e.end = end;

        stmts.add(begin);

        visit(e.body());

        stmts.add(end);
    }

    private Expr uvisit(ExprNode node) {
        return node.accept(this);
    }

    private Void uvisit(StmtNode node) {
        node.accept(this);
        return null;
    }

    private StringConstEntity addStringConst(String str) {
        StringConstEntity res =  ast.constantTable.get(str);
        if (res == null) {
            res = new StringConstEntity(str);
            ast.constantTable.put(str, res);
        }
        return res;
    }

    private Expr addCjumpOrReturn(boolean cond, ExprNode node, Expr obj) {
        if (cond) {
            Var tmp = createIntTmp();
            try { processAssign(tmp, obj); }
            catch (InternException ie) { errorHandler.error(node, ie.getMessage()); }
            stmts.add(new Cjump(tmp, node.ifTrue, node.ifFalse));
            return null;
        }
        else return obj;
    }

    @Override
    public Expr visit(UnaryOpNode node) {
        boolean needCjump = false;
        // For short-circuit
        if (node.ifTrue != null) {
            needCjump = true;
            if (node.operator().equals(UnaryOpNode.UnaryOp.LOGN)) {
                node.expr().ifTrue = node.ifFalse;
                node.expr().ifFalse = node.ifTrue;
            }
        }

        Expr res = null;
        switch (node.operator()) {
            case POS: res = uvisit(node.expr()); break;
            case NEG: {
                res = (node.expr() instanceof IntegerLiteralNode)?
                        new Imme(-((IntegerLiteralNode)node.expr()).value()):
                                new Uniop(Uniop.UniOp.NEG, uvisit(node.expr()));
                break;
            }
            case LOGN: case BITN:{
                ExprNode en = node.expr();
                if (en instanceof BoolLiteralNode)
                    res = new Imme(~(((BoolLiteralNode) en).value()? 1: 0));
                else if (en instanceof IntegerLiteralNode)
                    res = new Imme(~((IntegerLiteralNode) en).value());
                else
                    res = new Uniop(Uniop.UniOp.NOT, uvisit(node.expr()));
                break;
            }
            case PREM: case PREP:{
                Binop.BinOp op = node.operator() == UnaryOpNode.UnaryOp.PREP? Binop.BinOp.ADD: Binop.BinOp.SUB;
                Expr e = uvisit(node.expr());
                try {processAssign(e, new Binop(op, e, ONE));} catch (InternException ie) {errorHandler.error(node, ie.getMessage());}
                res = e;
                break;
            }
            case POSM: case POSP: {
                Var tmp = createIntTmp();
                Expr e = uvisit(node.expr());
                try {processAssign(tmp, e);} catch (InternException ie) {errorHandler.error(node, ie.getMessage());}
                Binop.BinOp op = node.operator() == UnaryOpNode.UnaryOp.PREP? Binop.BinOp.ADD: Binop.BinOp.SUB;
                try {processAssign(e, new Binop(op, e, ONE));} catch (InternException ie) {errorHandler.error(node, ie.getMessage());}
                res = tmp;
            }
        }
        return addCjumpOrReturn(needCjump, node, res);
    } // Finished

    @Override
    public Expr visit(BinaryOpNode node) {
        Label rightLabel = new Label("rhs__begin__");
        boolean needCjump = false;
        // For short-circuit
        if (node.ifTrue != null) {
            if (node.operator().equals(BinaryOpNode.BinaryOp.LOG_OR)) {
                node.left().ifTrue = node.ifTrue;
                node.left().ifFalse = rightLabel;
                node.right().ifTrue = node.ifTrue;
                node.right().ifFalse = node.ifFalse;
            }
            else if (node.operator().equals(BinaryOpNode.BinaryOp.LOG_AND)) {
                node.left().ifTrue = rightLabel;
                node.left().ifFalse = node.ifFalse;
                node.right().ifTrue = node.ifTrue;
                node.right().ifFalse = node.ifFalse;
            }
            else {
                needCjump = true;
                errorHandler.error(node, "Unknown operation for two bool var");
            }
        }

        Expr left = uvisit(node.left());
        if (node.ifTrue != null && !needCjump) stmts.add(rightLabel);
        Expr right = uvisit(node.right());

        Expr res = null;
        if (left instanceof Imme && right instanceof Imme) {
            int l = ((Imme) left).value;
            int r = ((Imme) right).value;
            switch (node.operator()) {
                case ADD: res = new Imme(l + r); break;
                case SUB: res = new Imme(l - r); break;
                case MUL: res = new Imme(l * r); break;
                case DIV: res = new Imme(l / r); break;
                case MOD: res = new Imme(l % r); break;
                case LE: res = new Imme(l <= r? 1: 0); break;
                case GE: res = new Imme(l >= r? 1: 0); break;
                case LT: res = new Imme(l < r? 1: 0); break;
                case GT: res = new Imme(l > r? 1: 0); break;
                case EQ: res = new Imme(l == r? 1: 0); break;
                case NE: res = new Imme(l != r? 1: 0); break;
                case XOR: res = new Imme(l ^ r); break;
                case LS: res = new Imme(l >> r); break;
                case RS: res = new Imme(l << r); break;
                case LOG_OR: case BIT_OR: res = new Imme(l | r); break;
                case LOG_AND: case BIT_AND: res = new Imme(l & r); break;
                default: errorHandler.error(node, "Ivalid binary operation for two Imme"); res = new Imme(0); break;
            }
        }
        else if (left instanceof Str && right instanceof Str) {
            String l = ((Str) left).entity.value;
            String r = ((Str) right).entity.value;
            switch (node.operator()) {
                case ADD: res = new Str(addStringConst(l + r)); break;
                case LE: res = new Imme(l.compareTo(r) <= 0? 1: 0); break;
                case GE: res = new Imme(l.compareTo(r) >= 0? 1: 0); break;
                case LT: res = new Imme(l.compareTo(r) < 0? 1: 0); break;
                case GT: res = new Imme(l.compareTo(r) > 0? 1: 0); break;
                case EQ: res = new Imme(l.compareTo(r) == 0? 1: 0); break;
                case NE: res = new Imme(l.compareTo(r) != 0? 1: 0); break;
                default: errorHandler.error(node, "Ivalid binary operation for two Str"); res = new Str(addStringConst("")); break;
            }
        }
        else if (node.left().type().equals(Type.STRING)) {
            String funcName = "__STRING__" + ".__FUNC__";
            List<Expr> args = new LinkedList<Expr>() {{
                add(left);
                add(right);
            }};

            switch (node.operator()) {
                case ADD: funcName += "add"; break;
                case LE: funcName += "le"; break;
                case GE: funcName += "de"; break;
                case LT: funcName += "lt"; break;
                case GT: funcName += "gt"; break;
                case EQ: funcName += "eq"; break;
                case NE: funcName += "ne"; break;
                default: errorHandler.error(node, "Ivalid binary operation for two string"); funcName += "UNKNOW"; break;
            }
            res = new Call(BuiltinFunction.get(funcName), args);
        }
        else {
            Binop.BinOp op;
            switch (node.operator()) {
                case ASSIGN: {
                    try { processAssign(left, right); }
                    catch (InternException ie) { errorHandler.error(node, ie.getMessage()); }
                    return null;
                }
                case ADD: op = Binop.BinOp.ADD; break;
                case SUB: op = Binop.BinOp.SUB; break;
                case LOG_AND: case BIT_AND: op = Binop.BinOp.AND; break;
                case LOG_OR:
                case BIT_OR: op = Binop.BinOp.OR; break;
                case LS: op = Binop.BinOp.LSH; break;
                case RS: op = Binop.BinOp.RSH; break;
                case MUL: op = Binop.BinOp.MUL; break;
                case DIV: op = Binop.BinOp.DIV; break;
                case MOD: op = Binop.BinOp.MOD; break;
                case XOR: op = Binop.BinOp.XOR; break;
                case LT: op = Binop.BinOp.LT; break;
                case GT: op = Binop.BinOp.GT; break;
                case LE: op = Binop.BinOp.LE; break;
                case GE: op = Binop.BinOp.GE; break;
                case EQ: op = Binop.BinOp.EQ; break;
                case NE: op = Binop.BinOp.NE; break;
                default: op = Binop.BinOp.EQ; errorHandler.error(node, "Unknown binary operator"); break;
            }
            res = new Binop(op, left, right);
        }
        return addCjumpOrReturn(needCjump, node, res);
    } // Finished

    @Override
    public Expr visit(FuncallNode node) {
        FunctionEntity entity = node.functionType().entity();
        List<Expr> args = new LinkedList<Expr>();
        for (ExprNode param : node.params())
            args.add(uvisit(param));

        return addCjumpOrReturn(node.ifTrue == null, node, new Call(entity, args));
    } // Finished

    @Override
    public Expr visit(BoolLiteralNode node) {
        return addCjumpOrReturn(node.ifTrue == null, node, new Imme(node.value()? 1 : 0));
    } // Finished

    @Override
    public Expr visit(NullLiteralNode node) {
        return new Imme(0);
    } // Finished

    @Override
    public Expr visit(IntegerLiteralNode node) {
        return new Imme(node.value());
    } // Finished

    @Override
    public Expr visit(StringLiteralNode node) {
        return new Str(addStringConst(node.value()));
    } // Finished

    @Override
    public Expr visit(ArefNode node) {
        ++arefDepth;
        Expr base = uvisit(node.base());
        Expr index = uvisit(node.index());
        --arefDepth;

        MoveAddr addr = new MoveAddr(base, index);
        Var tmp = createIntTmp();
        try { processAssign(tmp, addr); }
        catch (InternException ie) { errorHandler.error(node, ie.getMessage()); }
        return tmp;
    } // Finished

    @Override
    public Expr visit(MemberNode node) { // Finished
        Expr base = uvisit(node.field());
        if (node.entity() instanceof FunctionEntity)
            errorHandler.error(node, "Visiting class member function");
        int offset = node.entity().offset();
        return new MoveAddr(base, ZERO, new Imme(offset));
    } // Finished

    @Override
    public Expr visit(VariableNode node) {
        Entity entity = node.entity();
        if (entity.isMember()) {
            ClassEntity classEntity = entity.thisPtr();
            if (classEntity == null)
                errorHandler.error(node, "Class member without thisPtr");
            Expr base = new Addr(entity.thisPtr());
            int offset = entity.offset();
            return new MoveAddr(base, ZERO, new Imme(offset));
        }
        else return new Var(entity);
    } // Finished

    @Override
    public Expr visit(CreatorNode node) {
        Type type = node.type();
        FunctionEntity constructor = null;
        boolean isArray = type instanceof ArrayType;
        if (isArray) {
            Type baseType = ((ArrayType) type).baseType();
            if (baseType instanceof ClassType)
                constructor = ((ClassType) baseType).entity().constructor().entity();

        }
        return null;
    }

    @Override
    public Void visit(BlockNode node) {
        for (StmtNode stmt : node.stmts())
            uvisit(stmt);
        return null;
    } // Finished

    @Override
    public Void visit(BreakNode node) {
        stmts.add(new Jump(breakTarget));
        return null;
    } // Finished

    @Override
    public Void visit(ContinueNode node) {
        stmts.add(new Jump(continueTarget));
        return null;
    } // Finished

    @Override
    public Void visit(ExprStmtNode node) {
        uvisit(node.expr());
        return null;
    } // Finished

    @Override
    public Void visit(ForNode node) {
        Label condLabel = new Label("for_cond");
        Label bodyLabel = new Label("for_body");
        Label stepLabel = new Label("for_step");
        Label endLabel = new Label("for_end");

        Label tempStoreForContinueTarget = continueTarget;
        Label tempStoreForBreakTarget = breakTarget;

        continueTarget = stepLabel;
        breakTarget = endLabel;

        uvisit(node.init());

        stmts.add(condLabel);
        if (node.cond() != null) {
            node.cond().ifTrue = bodyLabel;
            node.cond().ifFalse = endLabel;
            uvisit(node.cond());
        }

        stmts.add(bodyLabel);
        if (node.body() != null) {
            uvisit(node.cond());
        }

        stmts.add(stepLabel);
        if (node.step() != null)
            uvisit(node.step());
        stmts.add(new Jump(condLabel));

        stmts.add(endLabel);

        continueTarget = tempStoreForContinueTarget;
        breakTarget = tempStoreForBreakTarget;
        return null;
    } // Finished

    @Override
    public Void visit(IfNode node) {
        Label thenLabel = new Label("if_then");
        Label elseLabel = new Label("if_else");
        Label endLabel = new Label("if_end");

        node.cond().ifTrue = thenLabel;
        node.cond().ifFalse = node.elseBody() != null? elseLabel : endLabel;
        uvisit(node.cond());

        stmts.add(thenLabel);
        if (node.thenBody() != null)
            uvisit(node.thenBody());

        stmts.add(elseLabel);
        if (node.elseBody() != null) {
            uvisit(node.elseBody());
            stmts.add(endLabel);
        }


        return null;
    } // Finished

    @Override
    public Void visit(ReturnNode node) {
        stmts.add(new Return(node.expr() == null? null: uvisit(node.expr())));
        stmts.add(new Jump(curFunc.end));
        return null;
    } // Finished

    @Override
    public Void visit(WhileNode node) {
        Label condLabel = new Label("while_cond");
        Label endLabel = new Label("while_end");
        Label bodyLabel = node.body() == null? endLabel: new Label("while_body");

        Label tempStoreForContinueTarget = continueTarget;
        Label tempStoreForBreakTarget = breakTarget;

        continueTarget = condLabel;
        breakTarget = endLabel;

        node.cond().ifTrue = bodyLabel;
        node.cond().ifFalse = endLabel;
        stmts.add(condLabel);
        uvisit(node.cond());

        stmts.add(bodyLabel);
        if (node.body() != null) {
            uvisit(node.body());
            stmts.add(endLabel);
        }

        continueTarget = tempStoreForContinueTarget;
        breakTarget = tempStoreForBreakTarget;
        return null;
    } // Finished

    @Override
    public Void visit(ClassDefNode node) {
        errorHandler.error(node, "Visiting class definition node");
        return null;
    } // Finished

    @Override
    public Void visit(FunctionDefNode node) {
        errorHandler.error(node, "Visiting function definition node");
        return null;
    } // Finished

    @Override
    public Void visit(VariableDefNode node) {
        VariableEntity entity = node.entity();
        ExprNode value = entity.value();
        if (value != null) {
            VariableNode varNode = new VariableNode(node.location(), entity.name());
            varNode.setEntity(entity);
            BinaryOpNode avatarNode = new BinaryOpNode(entity.type(), varNode, BinaryOpNode.BinaryOp.ASSIGN, value);
            visit(avatarNode);
        }
        return null;
    } // Finished
}