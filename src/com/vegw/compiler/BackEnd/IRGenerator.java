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
import com.vegw.compiler.Entity.ClassEntity;
import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.FrontEnd.LocalScope;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.IR.LinearIR.*;
import com.vegw.compiler.IR.LinearIR.Operand.*;
import com.vegw.compiler.Type.ArrayType;
import com.vegw.compiler.Type.ClassType;
import com.vegw.compiler.Type.FunctionType;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.BuiltinFunction;
import com.vegw.compiler.Utils.ErrorHandler;
import com.vegw.compiler.Utils.Location;

import java.util.*;

public class IRGenerator implements ASTVisitor<Void,Operand> {
    private final static Immediate ZERO = new Immediate(0);
    private final static Immediate ONE = new Immediate(1);
    private final static Immediate EIGHT = new Immediate(8);

    Register rax = PhysicalRegister.rax;
    Register rbx = PhysicalRegister.rbx;
    Register rcx = PhysicalRegister.rcx;
    Register rdx = PhysicalRegister.rdx;
    Register rsi = PhysicalRegister.rsi;
    Register rdi = PhysicalRegister.rdi;
    Register rbp = PhysicalRegister.rbp;
    Register rsp = PhysicalRegister.rsp;
    Register r8  = PhysicalRegister.r8;
    Register r9  = PhysicalRegister.r9;
    Register r10 = PhysicalRegister.r10;
    Register r11 = PhysicalRegister.r11;
    Register r12 = PhysicalRegister.r12;
    Register r13 = PhysicalRegister.r13;
    Register r14 = PhysicalRegister.r14;
    Register r15 = PhysicalRegister.r15;

    private Map<Entity, VirtualRegister> vrMap;
    protected ASTNode ast;
    private FunctionEntity curFunc;
    private Stack<Scope> stack = new Stack<Scope>();
    private Scope curScope;

    private boolean pushBeforeCall = false;
    private int labelCnt = 0;
    private int strCnt = 0;
    public RegisterList registerList;
    // malloc function
    private final static FunctionEntity malloc = BuiltinFunction.get("__FUNC__malloc");

    // Target of continue and break in loop
    private Label continueTarget = null;
    private Label breakTarget = null;

    // Multilayer arefNode depth
    private int arefDepth = 0;

    public List<FunctionEntity> funcs;
    public List<GlobalVarible> globalVars;

    // ======== Function =============

    private ErrorHandler errorHandler = new ErrorHandler("IRGenerator");

    // Create an virtual variable for self-inc or self-dec
    private int globalTmpCnt = 0;
    private VirtualRegister createIntTmp() {
        String name = "tmp" + String.valueOf(globalTmpCnt++);
        ExprNode value = null;
        LocalScope newScope = new LocalScope(curScope);
        VariableEntity entity = new VariableEntity(new Location(0,0), name, Type.INT);
        entity.setScope(newScope);
        VirtualRegister tmp = curFunc.getVReg(entity);
        return tmp;
    }

    private boolean hasLabel(ExprNode node) { return node.ifTrue != null || node.ifFalse != null; }

    private void processAssign(Operand dst, Operand src){
        if (dst == null || src == null)
            System.err.println("Assign operand is null");
        if (!(dst instanceof Register || dst instanceof Address || dst instanceof GlobalVarible))
            throw new Error("Invalid addressing object");
        curFunc.addIRInst(new Assign(dst, src));
    }

    public IRGenerator(ASTNode ast, RegisterList rList) {
        this.ast = ast;
        registerList = rList;
        stack.push(ast.scope);
        curScope = stack.peek();
        funcs = new LinkedList<FunctionEntity>();
        globalVars = new LinkedList<GlobalVarible>();
    }

    public void generate() {
        Map<String, Entity> map = ast.scope.entities();
        for (String key : map.keySet()) {
            Entity entity = map.get(key);
            if (entity instanceof ClassEntity)
                ((ClassEntity) entity).setOffset();
            else if (entity instanceof VariableEntity) {
                GlobalVarible gv = new GlobalVarible(entity.name());
                entity.setGlobalVariables(gv);
                globalVars.add(gv);
            }
        }

        String FIX;
        for (DefinitionNode def : ast.defs) {
            if (def instanceof VariableDefNode) continue;
            if (def instanceof FunctionDefNode) {
                curScope = ((FunctionEntity) def.entity()).scope();
                FunctionEntity e = ((FunctionDefNode) def).entity();
                funcs.add(e);
                curFunc = e;
                curFunc.pushAllParams();
                e.rename(e.name());
                compileFunction(e);
            }
            else {
                ClassEntity e = ((ClassDefNode) def).entity();
                curScope = e.scope();
                String name = e.name();
                e.rename(name);
                if (e.constructor() != null) {
                    FunctionDefNode func = e.constructor();
                    curScope = func.entity().scope();
                    FIX = name + ".";
                    FunctionEntity fe = ((FunctionDefNode) func).entity();
                    fe.setThisPtr(e);
                    fe.setMember(true);
                    funcs.add(fe);
                    curFunc = fe;
                    curFunc.pushAllParams();
                    fe.rename(FIX + fe.name());
                    compileFunction(fe);
                }
                for (FunctionDefNode func : e.funcs()) {
                    curScope = func.entity().scope();
                    FIX = name + "." ;
                    FunctionEntity fe = ((FunctionDefNode) func).entity();
                    fe.setThisPtr(e);
                    fe.setMember(true);
                    funcs.add(fe);
                    curFunc = fe;
                    curFunc.pushAllParams();
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

        curFunc.addIRInst(begin);
        // Initialization of global variables
        if (e.name().equals("main")) {
            for (DefinitionNode def : ast.defs) {
                if (def instanceof VariableDefNode)
                    uvisit(def);
            }
        }
        visit(e.body());
        if (!(curFunc.getLastIRInst() instanceof Return))
            curFunc.addIRInst(new Jump(end));
        curFunc.addIRInst(end);
        curFunc.addIRInst(new Return());
    }

    private Operand uvisit(ExprNode node) {
        return node.accept(this);
    }

    private Void uvisit(StmtNode node) {
        node.accept(this);
        return null;
    }

    private Map<Integer, Integer> strmap = new HashMap<Integer, Integer>(){{
        put(92, 92);   //     \\
        put(97, 7);    //     \a
        put(98, 8);    //     \b
        put(48, 0);    //     \0
        put(116, 9);   //     \t
        put(110, 10);  //     \n
        put(118, 11);  //     \v
        put(102, 12);  //     \f
        put(114, 13);  //     \r
        put(34, 34);  //      \"
    }};

    private String addStringConst(String str) {
        StringBuffer nstr = new StringBuffer();
        for (int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) == 92) {
                int c = str.charAt(++i);
                int a = (int) strmap.get(c);
                nstr.append((char) a);
            }
            else
                nstr.append((char) str.charAt(i));
        }

        String newStr = nstr.toString();
        String res =  ast.constantTable.get(newStr);
        if (res == null) {
            res = "__staticString__"+ strCnt;
            strCnt++;
            ast.constantTable.put(newStr, res);
        }
        return res;
    }

    @Override
    public Operand visit(UnaryOpNode node) {
        // For short-circuit
        if (hasLabel(node)) {
            if (node.operator().equals(UnaryOpNode.UnaryOp.LOGN)) {
                node.expr().ifTrue = node.ifFalse;
                node.expr().ifFalse = node.ifTrue;
            }
        }
        
        VirtualRegister tmp = createIntTmp();
        Operand res = null;
        Operand operand = uvisit(node.expr());
        switch (node.operator()) {
            case POS: res = operand; break;
            case NEG: {
                if (operand instanceof Immediate)
                    res = new Immediate(-((Immediate) operand).value);
                else {
                    processAssign(tmp, operand);
                    curFunc.addIRInst(new Uniop(Uniop.UniOp.NEG, tmp));
                    res = tmp;
                }
                break;
            }
            case LOGN:{
                if (operand instanceof Immediate)
                    res = new Immediate(~((Immediate) operand).value);
                else {
                    if (hasLabel(node)) return null;
                    processAssign(tmp, operand);
                    curFunc.addIRInst(new Uniop(Uniop.UniOp.LNOT, tmp));
                    res = tmp;
                }
                break;
            }
            case BITN: {
                if (operand instanceof Immediate)
                    res = new Immediate(~((Immediate) operand).value);
                else {
                    processAssign(tmp, operand);
                    curFunc.addIRInst(new Uniop(Uniop.UniOp.BNOT, tmp));
                    res = tmp;
                }
                break;
            }
            case PREM: case PREP:{
                Binop.BinOp op = node.operator() == UnaryOpNode.UnaryOp.PREP? Binop.BinOp.ADD: Binop.BinOp.SUB;
                curFunc.addIRInst(new Binop(op, operand, ONE));
                res = operand;
                break;
            }
            case POSM: case POSP: {
                processAssign(tmp, operand);
                Binop.BinOp op = node.operator() == UnaryOpNode.UnaryOp.POSP? Binop.BinOp.ADD: Binop.BinOp.SUB;
                curFunc.addIRInst(new Binop(op, operand, ONE));
                res = tmp;
            }
        }
        return res;
    } // Finished

//    private boolean shortCircuitAssign(BinaryOpNode node) {
//        Location loc = node.location();
//        BoolLiteralNode T = new BoolLiteralNode(loc, true);
//        BoolLiteralNode F = new BoolLiteralNode(loc, false);
//        if (node.operator().equals(BinaryOpNode.BinaryOp.ASSIGN)) {
//            if (node.right() instanceof BinaryOpNode) {
//                BinaryOpNode.BinaryOp op = ((BinaryOpNode) node.right()).operator();
//                if (op.equals(BinaryOpNode.BinaryOp.LOG_AND) ||
//                        ((BinaryOpNode) node.right()).operator().equals(BinaryOpNode.BinaryOp.LOG_OR)) {
//                    BlockNode thenBlock = new BlockNode(node.location(), new LinkedList<StmtNode>() {{
//                        add(new ExprStmtNode(new BinaryOpNode(Type.BOOL, node.left(), BinaryOpNode.BinaryOp.ASSIGN, T)));
//                    }});
//                    BlockNode elseBlock = new BlockNode(loc, new LinkedList<StmtNode>() {{
//                        add(new ExprStmtNode(new BinaryOpNode(Type.BOOL, node.left(), BinaryOpNode.BinaryOp.ASSIGN, F)));
//                    }});
//                    IfNode avateorNode = new IfNode(node.location(), node.right(), thenBlock, elseBlock);
//                    uvisit(avateorNode);
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    private int islog(Immediate imm) {
        int cnt = 0;
        int a = imm.value;
        while (a % 2 == 0) {
            cnt += 1;
            a /= 2;
        }
        if (a > 1) return -1;
        else return cnt;
    }

    public Operand visit(BinaryOpNode node) {
        Location loc = node.location();
        BoolLiteralNode T = new BoolLiteralNode(loc, true);
        BoolLiteralNode F = new BoolLiteralNode(loc, false);
        if (node.operator().equals(BinaryOpNode.BinaryOp.ASSIGN)) {
            if (node.left().type().equals(Type.BOOL) && !(node.right() instanceof BoolLiteralNode)) {
                BlockNode thenBlock = new BlockNode(node.location(), new LinkedList<StmtNode>() {{
                    add(new ExprStmtNode(new BinaryOpNode(Type.BOOL, node.left(), BinaryOpNode.BinaryOp.ASSIGN, T)));
                }});
                BlockNode elseBlock = new BlockNode(loc, new LinkedList<StmtNode>() {{
                    add(new ExprStmtNode(new BinaryOpNode(Type.BOOL, node.left(), BinaryOpNode.BinaryOp.ASSIGN, F)));
                }});
                IfNode avateorNode = new IfNode(node.location(), node.right(), thenBlock, elseBlock);
                visit(avateorNode);
                return null;
            }
        }

        Label rightLabel = new Label("rhs__begin__" + labelCnt);
        labelCnt++;
        boolean needCjump = false;

        // For short-circuit jump
        if (hasLabel(node)) {
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
            else
                needCjump = true;
        }

        VirtualRegister tmp = createIntTmp();
        Operand res = null;

        Operand left = uvisit(node.left());
        if (hasLabel(node) && !needCjump) curFunc.addIRInst(rightLabel);
        Operand right = uvisit(node.right());

        if (left instanceof Immediate && right instanceof Immediate) {
            int l = ((Immediate) left).value;
            int r = ((Immediate) right).value;
            switch (node.operator()) {
                case ADD: res = new Immediate(l + r); break;
                case SUB: res = new Immediate(l - r); break;
                case MUL:res = new Immediate(l * r); break;
                case DIV: res = new Immediate(l / r); break;
                case MOD: res = new Immediate(l % r); break;
                case LE: res = new Immediate(l <= r? 1: 0); break;
                case GE: res = new Immediate(l >= r? 1: 0); break;
                case LT: res = new Immediate(l < r? 1: 0); break;
                case GT: res = new Immediate(l > r? 1: 0); break;
                case EQ: res = new Immediate(l == r? 1: 0); break;
                case NE: res = new Immediate(l != r? 1: 0); break;
                case XOR: res = new Immediate(l ^ r); break;
                case LS: res = new Immediate(l << r); break;
                case RS: res = new Immediate(l >> r); break;
                case LOG_OR: case BIT_OR: res = new Immediate(l | r); break;
                case LOG_AND: case BIT_AND: res = new Immediate(l & r); break;
                default: errorHandler.error(node, "Ivalid binary operation for two Immediate"); res = new Immediate(0); break;
            }
            if (hasLabel(node)) {
                if (((Immediate) res).value == 1 && node.ifTrue != null)
                    curFunc.addIRInst(new Jump((node.ifTrue)));
                if (((Immediate) res).value == 0 && node.ifFalse != null)
                    curFunc.addIRInst(new Jump((node.ifFalse)));
                return null;
            }
        }
        else if (left instanceof Str && right instanceof Str) {
            String l = ((Str) left).value;
            String r = ((Str) right).value;
            switch (node.operator()) {
                case ADD: res = new Str(l.substring(1,l.length()-1) + r.substring(1, r.length()-1),addStringConst(l.substring(1,l.length()-1) + r.substring(1, r.length()-1))); break;
                case LE: res = new Immediate(l.compareTo(r) <= 0? 1: 0); break;
                case GE: res = new Immediate(l.compareTo(r) >= 0? 1: 0); break;
                case LT: res = new Immediate(l.compareTo(r) < 0? 1: 0); break;
                case GT: res = new Immediate(l.compareTo(r) > 0? 1: 0); break;
                case EQ: res = new Immediate(l.compareTo(r) == 0? 1: 0); break;
                case NE: res = new Immediate(l.compareTo(r) != 0? 1: 0); break;
                default: errorHandler.error(node, "Ivalid binary operation for two Str"); res = new Str("", addStringConst("")); break;
            }
            if (hasLabel(node)) {
                if (((Immediate) res).value == 1 && node.ifTrue != null)
                    curFunc.addIRInst(new Jump(node.ifTrue));
                if (((Immediate) res).value == 0 && node.ifFalse != null)
                    curFunc.addIRInst(new Jump(node.ifFalse));
                return null;
            }
        }
        else if (node.left().type().equals(Type.STRING)) {
            if (node.operator().equals(BinaryOpNode.BinaryOp.ASSIGN)) {
                processAssign(left, right);
                return null;
            }

            if (pushBeforeCall) {
                for (int i = 0; i < 6; ++i) {
                    curFunc.addIRInst(new Push(registerList.callerSavedRegs.get(i)));
                }
            }


            String funcName = "string.";
            processAssign(rdi, left);
            processAssign(rsi, right);

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
            curFunc.addIRInst(new Call(BuiltinFunction.get(funcName)));
            if (hasLabel(node)) {
                Binop cond = new Binop(Binop.BinOp.NE, rax, ZERO);
                curFunc.addIRInst(new Cjump(cond, node.ifTrue, node.ifFalse));
                return null;
            }
            processAssign(tmp, rax);
            curFunc.callOtherFunc = true;
            res = tmp;
        }
        else if (right instanceof Immediate &&
                (node.operator().equals(BinaryOpNode.BinaryOp.MUL) ||
                        node.operator().equals(BinaryOpNode.BinaryOp.DIV)) &&
                islog((Immediate) right) > 0) {
            processAssign(tmp, left);
            if (node.operator().equals(BinaryOpNode.BinaryOp.MUL))
                curFunc.addIRInst(new Binop(Binop.BinOp.LSH, tmp, new Immediate(islog((Immediate) right))));
            else
                curFunc.addIRInst(new Binop(Binop.BinOp.RSH, tmp, new Immediate(islog((Immediate) right))));
            res = tmp;
        }
        else {
            Binop.BinOp op;
            boolean basicBool = false;
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
                case LT: op = Binop.BinOp.LT; basicBool = true; break;
                case GT: op = Binop.BinOp.GT; basicBool = true; break;
                case LE: op = Binop.BinOp.LE; basicBool = true; break;
                case GE: op = Binop.BinOp.GE; basicBool = true; break;
                case EQ: op = Binop.BinOp.EQ; basicBool = true; break;
                case NE: op = Binop.BinOp.NE; basicBool = true; break;
                default: op = Binop.BinOp.EQ; errorHandler.error(node, "Unknown binary operator"); break;
            }
            // The min unit of bool (a rel b)
            if (basicBool && hasLabel(node)) {
                curFunc.addIRInst(new Cjump(new Binop(op, left, right), node.ifTrue, node.ifFalse));
                return null;
            }
            if (node.operator().equals(BinaryOpNode.BinaryOp.LOG_AND) || node.operator().equals(BinaryOpNode.BinaryOp.LOG_OR))
                if (left == null && right == null)
                    return null;
            if (hasLabel(node)) return null;
            processAssign(tmp, left);
            curFunc.addIRInst(new Binop(op, tmp, right));
            res = tmp;
        }
        return res;
    } // Finished

    @Override
    public Operand visit(FuncallNode node) {
        Operand operand = uvisit(node.name());
        FunctionEntity entity = node.functionType().entity();
        List<Operand> args = new LinkedList<Operand>();

        if (((FunctionType)node.name().type()).entity().thisPtr() != null || node.name() instanceof MemberNode ) {
            if (!(node.name() instanceof MemberNode) && curFunc.thisPtr() == ((FunctionType)node.name().type()).entity().thisPtr())
                args.add(createAddress(rbp, null , new Immediate(-8)));
            else
                args.add(operand);
        }

        for (int i = 0; i < node.params().size(); ++i)
            args.add(uvisit(node.params().get(i)));

        if (pushBeforeCall) {
            for (int i = 0; i < 6; ++i) {
                curFunc.addIRInst(new Push(registerList.callerSavedRegs.get(i)));
            }
        }
        for (int i = args.size() - 1; i >= 0; --i) {
            if (i < 6) processAssign(registerList.paramRegs.get(i), args.get(i));
            else curFunc.addIRInst(new Push(args.get(i)));
        }

        curFunc.addIRInst(new Call(entity));

        if (hasLabel(node)) {
            curFunc.addIRInst(new Cjump(new Binop(Binop.BinOp.NE, rax, ZERO), node.ifTrue, node.ifFalse));
            return null;
        }
        else {
            VirtualRegister tmp = createIntTmp();
            processAssign(tmp, rax);
            return tmp;
        }
    } // Finished

    @Override
    public Operand visit(BoolLiteralNode node) {
        Immediate res = new Immediate(node.value()? 1 : 0);
        if (hasLabel(node)) {
            if (res.value == 1 && node.ifTrue != null)
                curFunc.addIRInst(new Jump(node.ifTrue));
            if (res.value == 0 && node.ifFalse != null)
                curFunc.addIRInst(new Jump(node.ifFalse));
            return null;
        }
        return res;
    } // Finished

    @Override
    public Operand visit(NullLiteralNode node) {
        return new Immediate(0);
    } // Finished

    @Override
    public Operand visit(IntegerLiteralNode node) {
        return new Immediate(node.value());
    } // Finished

    @Override
    public Operand visit(StringLiteralNode node) {
        String str = node.value();
        str = str.substring(1, str.length() - 1);
        return new Str(addStringConst(str), node.value());
    } // Finished

    @Override
    public Operand visit(ArefNode node) {
        ++arefDepth;
        Operand base = uvisit(node.base());
        Operand index = uvisit(node.index());
        --arefDepth;

        Address addr = createAddress(base, index, null);
        if (arefDepth == 0 && hasLabel(node))
            curFunc.addIRInst(new Cjump(new Binop(Binop.BinOp.NE, addr, ZERO), node.ifTrue, node.ifFalse));
        return addr;
    } // Finished

    @Override
    public Operand visit(MemberNode node) { // Finished
        Operand base = uvisit(node.field());
        if (node.entity() instanceof FunctionEntity)
            return base;
        int offset = node.entity().offset();
        Address addr = createAddress(base, null, new Immediate(offset));
        if (hasLabel(node)){
            Binop cond = new Binop(Binop.BinOp.NE, addr, ZERO);
            curFunc.addIRInst(new Cjump(cond, node.ifTrue, node.ifFalse));
        }
        return addr;
    } // Finished

    @Override
    public Operand visit(VariableNode node) {
        Entity entity = node.entity();
        boolean isGV = entity.isGlobalVariables();
        GlobalVarible gv = entity.gvalue;
        if (hasLabel(node)) {
            curFunc.addIRInst(new Cjump(new Binop(Binop.BinOp.NE, isGV? gv:curFunc.getVReg(entity), ZERO), node.ifTrue, node.ifFalse));
            return null;
        }
        if (entity.isMember() && !(entity instanceof FunctionEntity)) {
            Entity thisEntity = curScope.entities().get("this");
            if (thisEntity == null)
                errorHandler.error(node, "Class member without thisPtr");
            VirtualRegister base = curFunc.getVReg(thisEntity);
            int offset = entity.offset();
//            VirtualRegister tmp = createIntTmp();
//            processAssign(tmp, base);
            return createAddress(base, null, new Immediate(offset));
        }
        else return isGV? gv:curFunc.getVReg(entity);
    } // Finished

    private void createArray(List<Operand> dimensionArgs, Operand dst, int now) {
        VirtualRegister nowSubscript = createIntTmp();
        VirtualRegister maxSubscript = createIntTmp();
        Label dimensionBodyLabel = new Label("dimension_body" + labelCnt);
        Label dimensionEndLabel = new Label("dimension_end" + labelCnt);
        labelCnt++;
        processAssign(nowSubscript, ZERO);
        processAssign(maxSubscript, (Operand) dimensionArgs.get(now));
        processAssign(rax, maxSubscript);
        curFunc.addIRInst(new Binop(Binop.BinOp.ADD, rax, ONE));
        curFunc.addIRInst(new Binop(Binop.BinOp.LSH, rax, new Immediate(3)));
        processAssign(rdi, rax);
        if (pushBeforeCall) {
            for (int i = 0; i < 6; ++i) {
                curFunc.addIRInst(new Push(registerList.callerSavedRegs.get(i)));
            }
        }
        curFunc.addIRInst(new Call(malloc));
        curFunc.addIRInst(new Binop(Binop.BinOp.ADD, rax, EIGHT));
        processAssign(dst, rax);
        processAssign(createAddress(dst, null, new Immediate(-8)), (Operand) dimensionArgs.get(now));

        if (dimensionArgs.size() > now + 1) {
            VirtualRegister tmp = createIntTmp();
            processAssign(tmp, dst);
//            curFunc.addIRInst(new Push(tmp));
            curFunc.addIRInst(dimensionBodyLabel);
            createArray(dimensionArgs, createAddress(tmp, null, null), now + 1);
//            curFunc.addIRInst(new Pop);
            curFunc.addIRInst(new Binop(Binop.BinOp.ADD, tmp, EIGHT));
            curFunc.addIRInst(new Binop(Binop.BinOp.ADD, nowSubscript, ONE));
            curFunc.addIRInst(new Cjump(new Binop(Binop.BinOp.LT, nowSubscript, maxSubscript),
                    dimensionBodyLabel, null));
            curFunc.addIRInst(dimensionEndLabel);
        }
    }

    @Override
    public Operand visit(CreatorNode node) {
        Type type = node.type();
        FunctionEntity constructor = null;
        List<Operand> dimensionArgs = new LinkedList<Operand>();
        for (ExprNode Operand : node.dimensionExpr())
            dimensionArgs.add(uvisit(Operand));

        VirtualRegister tmp = createIntTmp();
        boolean isArray = type instanceof ArrayType;
        if (isArray) {
            if (dimensionArgs.size() > 0)
                createArray(dimensionArgs, tmp, 0);
        }
        else {
            processAssign(rdi, new Immediate(((ClassType) type).entity().size()));
            if (pushBeforeCall) {
                for (int i = 0; i < 6; ++i) {
                    curFunc.addIRInst(new Push(registerList.callerSavedRegs.get(i)));
                }
            }
            curFunc.addIRInst(new Call(malloc));
            processAssign(tmp, rax);
            if (((ClassType) type).entity().constructor() != null) {
                constructor = ((ClassType) type).entity().constructor().entity();
                if (pushBeforeCall) {
                    for (int i = 0; i < 6; ++i) {
                        curFunc.addIRInst(new Push(registerList.callerSavedRegs.get(i)));
                    }
                }
                processAssign(rdi, tmp);
                curFunc.addIRInst(new Call(constructor));
            }
        }
        return tmp;
    }

    @Override
    public Void visit(BlockNode node) {
        for (StmtNode n : node.stmts())
            uvisit(n);
        return null;
    }

    @Override
    public Void visit(BreakNode node) {
        curFunc.addIRInst(new Jump(breakTarget));
        return null;
    }

    @Override
    public Void visit(ContinueNode node) {
        curFunc.addIRInst(new Jump(continueTarget));
        return null;
    }

    @Override
    public Void visit(ExprStmtNode node) {
        uvisit(node.expr());
        return null;
    }

    @Override
    public Void visit(ForNode node) {
        Label condLabel = new Label("for_cond" + labelCnt);
        Label bodyLabel = new Label("for_body" + labelCnt);
        Label stepLabel = new Label("for_step" + labelCnt);
        Label endLabel = new Label("for_end" + labelCnt);
        labelCnt++;
        Label tempStoreForContinueTarget = continueTarget;
        Label tempStoreForBreakTarget = breakTarget;

        continueTarget = stepLabel;
        breakTarget = endLabel;

        if (node.init() != null)
            uvisit(node.init());

        curFunc.addIRInst(condLabel);
        if (node.cond() != null) {
            node.cond().ifTrue = bodyLabel;
            node.cond().ifFalse = endLabel;
            uvisit(node.cond());
        }

        curFunc.addIRInst(bodyLabel);
        if (node.body() != null) {
            uvisit(node.body());
        }

        curFunc.addIRInst(stepLabel);
        if (node.step() != null)
            uvisit(node.step());
        curFunc.addIRInst(new Jump(condLabel));

        curFunc.addIRInst(endLabel);

        continueTarget = tempStoreForContinueTarget;
        breakTarget = tempStoreForBreakTarget;
        return null;
    } // Finished

    @Override
    public Void visit(IfNode node) {
        Label thenLabel = new Label("if_then" + labelCnt);
        Label elseLabel = new Label("if_else" + labelCnt);
        Label endLabel = new Label("if_end" + labelCnt);
        labelCnt++;
        node.cond().ifTrue = thenLabel;
        node.cond().ifFalse = node.elseBody() != null? elseLabel : endLabel;
        uvisit(node.cond());

        curFunc.addIRInst(thenLabel);
        if (node.thenBody() != null) {
            uvisit(node.thenBody());
            curFunc.addIRInst(new Jump(endLabel));
        }

        if (node.elseBody() != null) {
            curFunc.addIRInst(elseLabel);
            uvisit(node.elseBody());
        }

        curFunc.addIRInst(endLabel);
        return null;
    } // Finished

    @Override
    public Void visit(ReturnNode node) {
        if (node.expr() != null)
            processAssign(rax, uvisit(node.expr()));
        curFunc.addIRInst(new Jump(curFunc.end));
        return null;
    } // Finished

    @Override
    public Void visit(WhileNode node) {
        Label condLabel = new Label("while_cond" + labelCnt);
        Label endLabel = new Label("while_end" + labelCnt);
        Label bodyLabel = node.body() == null? endLabel: new Label("while_body" + labelCnt);

        Label tempStoreForContinueTarget = continueTarget;
        Label tempStoreForBreakTarget = breakTarget;

        continueTarget = condLabel;
        breakTarget = endLabel;

        node.cond().ifTrue = bodyLabel;
        node.cond().ifFalse = endLabel;
        curFunc.addIRInst(condLabel);
        uvisit(node.cond());


        curFunc.addIRInst(bodyLabel);
        if (node.body() != null) {
            uvisit(node.body());
            curFunc.addIRInst(new Jump(condLabel));
        }

        curFunc.addIRInst(endLabel);

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

    private Address createAddress(Operand base, Operand index, Immediate offset) {
        if (base instanceof Address || index instanceof Address ||
                base instanceof GlobalVarible || index instanceof GlobalVarible) {
            VirtualRegister tmp = createIntTmp();
            if (index != null) {
                processAssign(tmp, index);
                curFunc.addIRInst(new Binop(Binop.BinOp.LSH, tmp, new Immediate(3)));
                curFunc.addIRInst(new Binop(Binop.BinOp.ADD, tmp, base));
            } else processAssign(tmp, base);
            return new Address(tmp, null, offset);
        }
        return new Address(base, index, offset);
    }
}