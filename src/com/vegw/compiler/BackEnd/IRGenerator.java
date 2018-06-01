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
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.FrontEnd.LocalScope;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.IR.LinearIR.*;
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
    private final static Imme EIGHT = new Imme(8);

    private List<IRInstruction> stmts;
    protected ASTNode ast;
    private FunctionEntity curFunc;
    private Stack<Scope> stack;
    private Scope curScope;

    private int labelCnt = 0;

    // malloc function
    private final static FunctionEntity malloc = BuiltinFunction.get("__FUNC__malloc");

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

    private Expr getAddr(Expr expr){
        Expr addr;
        if (expr instanceof Var)
            addr = new Addr(((Var) expr).entity);
        else if (expr instanceof Mem)
            addr = expr;
        else
            throw new Error("Invalid addressing object");
        return addr;
    }

    private void processAssign(Expr dst, Expr src){
        stmts.add(new Assign(getAddr(dst), src));
    }

    public IRGenerator(ASTNode ast) {
        this.stmts = new LinkedList<IRInstruction>();
        this.ast = ast;
        stack.push(ast.scope);
        curScope = stack.peek();
    }

    public List<IRInstruction> getStmts() { return this.stmts; }

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
                    fe.setThisPtr(e);
                    fe.setMember(true);
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
        // Initialization of global variables
        if (e.name().equals("main")) {
            for (DefinitionNode def : ast.defs) {
                if (def instanceof VariableDefNode)
                    uvisit(def);
            }
        }
        visit(e.body());
        if (!(e.body().stmts().get(e.body().stmts().size() - 1) instanceof ReturnNode))
            stmts.add(new Jump(end));
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
            Var tmp;
            if (!(obj instanceof Var)) {
                tmp = createIntTmp();
                processAssign(tmp, obj);
            }
            else tmp = (Var) obj;
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
            if (node.operator().equals(UnaryOpNode.UnaryOp.LOGN)) {
                node.expr().ifTrue = node.ifFalse;
                node.expr().ifFalse = node.ifTrue;
            }
            else needCjump = true;
        }


        Var tmp = createIntTmp();
        Expr res = null;
        switch (node.operator()) {
            case POS: res = uvisit(node.expr()); break;
            case NEG: {
                if (node.expr() instanceof IntegerLiteralNode)
                    res = new Imme(-((IntegerLiteralNode)node.expr()).value());
                else {
                    processAssign(tmp, new Uniop(Uniop.UniOp.NEG, uvisit(node.expr())));
                    res = tmp;
                }
                break;
            }
            case LOGN:{
                ExprNode en = node.expr();
                if (en instanceof BoolLiteralNode)
                    res = new Imme(~(((BoolLiteralNode) en).value()? 1: 0));
                else {
                    processAssign(tmp, new Uniop(Uniop.UniOp.LNOT, uvisit(node.expr())));
                    res = tmp;
                }
                break;
            }
            case BITN: {
                ExprNode en = node.expr();
                if (en instanceof IntegerLiteralNode)
                    res = new Imme(~((IntegerLiteralNode) en).value());
                else {
                    processAssign(tmp, new Uniop(Uniop.UniOp.BNOT, uvisit(node.expr())));
                    res = tmp;
                }
                break;
            }
            case PREM: case PREP:{
                Binop.BinOp op = node.operator() == UnaryOpNode.UnaryOp.PREP? Binop.BinOp.ADD: Binop.BinOp.SUB;
                Expr e = uvisit(node.expr());
                processAssign(e, new Binop(op, e, ONE));
                res = e;
                break;
            }
            case POSM: case POSP: {
                Expr e = uvisit(node.expr());
                processAssign(tmp, e);
                Binop.BinOp op = node.operator() == UnaryOpNode.UnaryOp.PREP? Binop.BinOp.ADD: Binop.BinOp.SUB;
                processAssign(e, new Binop(op, e, ONE));
                res = tmp;
            }
        }
        return addCjumpOrReturn(needCjump, node, res);
    } // Finished

    @Override
    public Expr visit(BinaryOpNode node) {
        Label rightLabel = new Label(labelCnt + "rhs__begin__");
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

        Var tmp = createIntTmp();
        Expr res;
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

            Var tmpLeft;
            if (left instanceof Var)
                tmpLeft = (Var) left;
            else {
                tmpLeft = createIntTmp();
                processAssign(tmpLeft, left);
            }

            Var tmpRight;
            if (right instanceof Var)
                tmpRight = (Var) right;
            else {
                tmpRight = createIntTmp();
                processAssign(tmpRight, right);
            }

            List<Var> args = new LinkedList<Var>() {{
                add(tmpLeft);
                add(tmpRight);
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
            processAssign(tmp, new Call(BuiltinFunction.get(funcName), args));
            res = tmp;
        }
        else {
            Binop.BinOp op;
            switch (node.operator()) {
                case ASSIGN: {
                    processAssign(left, right);
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
            processAssign(tmp, new Binop(op, left, right));
            res = tmp;
        }
        return addCjumpOrReturn(needCjump, node, res);
    } // Finished

    @Override
    public Expr visit(FuncallNode node) {

        FunctionEntity entity = node.functionType().entity();
        List<Var> args = new LinkedList<Var>();
        for (ExprNode param : node.params()) {
            Expr expr = uvisit(param);
            if (!(expr instanceof Var)) {
                Var paramTmp = createIntTmp();
                processAssign(paramTmp, expr);
                args.add(paramTmp);
            }
            else args.add((Var) expr);
        }
        if (node.name() instanceof MemberNode) {
            Var addrTmp = createIntTmp();
            processAssign(addrTmp, getAddr(uvisit(node.name())));
            ((LinkedList<Var>) args).addFirst(addrTmp);
        }
        Var tmp = createIntTmp();
        processAssign(tmp, new Call(entity, args));
        return addCjumpOrReturn(node.ifTrue == null, node, tmp);
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

        Mem addr = new Mem(base, index);
        if (arefDepth > 0) {
            Var tmp = createIntTmp();
            processAssign(tmp, addr);
            return tmp;
        }
        else return addr;
    } // Finished

    @Override
    public Expr visit(MemberNode node) { // Finished
        Expr base = uvisit(node.field());
        if (node.entity() instanceof FunctionEntity)
            return base;
        int offset = node.entity().offset();
        return new Mem(base, null, new Imme(offset));
    } // Finished

    @Override
    public Expr visit(VariableNode node) {
        Entity entity = node.entity();
        if (entity.isMember()) {
            ClassEntity classEntity = entity.thisPtr();
            if (classEntity == null)
                errorHandler.error(node, "Class member without thisPtr");
            Expr base = new Addr(classEntity);
            int offset = entity.offset();
            return new Mem(base, null, new Imme(offset));
        }
        else return new Var(entity);
    } // Finished

    private void createArray(List<Expr> dimensionArgs, Expr dst, int now, int allLayer, Type type, FunctionEntity constructor) {
        Var nowSubscript = createIntTmp();
        Var maxSubscript = createIntTmp();
        Label dimensionBodyLabel = new Label(labelCnt + "dimension_body");
        Label dimensionEndLabel = new Label(labelCnt + "dimension_end");
        processAssign(nowSubscript, ZERO);
        processAssign(maxSubscript, (Expr) dimensionArgs.get(now));
        List<Var> mallocArgs = new LinkedList<Var>() {{
            Var tmp = createIntTmp();
            processAssign(tmp, new Binop(Binop.BinOp.ADD, maxSubscript, ONE));
            processAssign(tmp, new Binop(Binop.BinOp.MUL, tmp, EIGHT));
            add(tmp);
        }};
        processAssign(dst, new Call(malloc, mallocArgs));
        stmts.add(dimensionBodyLabel);
        Mem daddr = new Mem(dst, nowSubscript, EIGHT);

        if (dimensionArgs.size() > now + 1)
            createArray(dimensionArgs, daddr, now + 1, allLayer, type, constructor);
        else if (allLayer == now + 1 && type instanceof ClassType && constructor != null) {
            Var tmp = createIntTmp();
            processAssign(tmp, new Call(malloc, new LinkedList<Var>(){{
                Var mallocTmp = createIntTmp();
                processAssign(mallocTmp, new Imme(((ClassType) type).entity().size()));
                add(mallocTmp);
            }}));
            stmts.add(new Call(constructor, new LinkedList<Var>(){{add(tmp);}}));
            processAssign(daddr, tmp);
        }

        processAssign(nowSubscript, new Binop(Binop.BinOp.ADD, nowSubscript, ONE));
        stmts.add(new Cjump(new Binop(Binop.BinOp.LT, nowSubscript, maxSubscript),
                dimensionBodyLabel, null));
        stmts.add(dimensionEndLabel);
    }
    @Override
    public Expr visit(CreatorNode node) {
        Type type = node.type();
        FunctionEntity constructor = null;
        List<Expr> dimensionArgs = new LinkedList<Expr>();
        for (ExprNode expr : node.dimensionExpr())
            dimensionArgs.add(uvisit(expr));

        Var tmp = createIntTmp();
        boolean isArray = type instanceof ArrayType;
        if (isArray) {
            Type baseType = ((ArrayType) type).baseType();
            if (baseType instanceof ClassType)
                constructor = ((ClassType) baseType).entity().constructor().entity();

            if (dimensionArgs.size() > 0)
                createArray(dimensionArgs, tmp, 0, ((ArrayType) type).demension(), baseType, constructor);
        }
        else {
            processAssign(tmp, new Call(malloc, new LinkedList<Var>(){{
                Var mallocTmp = createIntTmp();
                processAssign(mallocTmp, new Imme(((ClassType) type).entity().size()));
                add(mallocTmp);
            }}));
            constructor = ((ClassType) type).entity().constructor().entity();
            stmts.add(new Call(constructor, new LinkedList<Var>(){{add(tmp);}}));
        }
        return tmp;
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
        Label condLabel = new Label(labelCnt + "for_cond");
        Label bodyLabel = new Label(labelCnt + "for_body");
        Label stepLabel = new Label(labelCnt + "for_step");
        Label endLabel = new Label(labelCnt + "for_end");

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
        Label thenLabel = new Label(labelCnt + "if_then");
        Label elseLabel = new Label(labelCnt + "if_else");
        Label endLabel = new Label(labelCnt + "if_end");

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
        Label condLabel = new Label(labelCnt + "while_cond");
        Label endLabel = new Label(labelCnt + "while_end");
        Label bodyLabel = node.body() == null? endLabel: new Label(labelCnt + "while_body");

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