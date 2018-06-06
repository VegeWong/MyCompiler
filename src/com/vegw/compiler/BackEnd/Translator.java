package com.vegw.compiler.BackEnd;

import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.IR.LinearIR.*;
import com.vegw.compiler.IR.LinearIR.Operand.*;

import java.util.*;

public class Translator {
    private Map<Entity, String> map;
    private IRGenerator irGenerator;
    public List<String> list = new LinkedList<>();
    private FunctionEntity curFunc;
    private int offset;
//    private Stack<Map<String, Integer> > offsetMapStack;
//    private Map<String, Integer> curOffsetMap;
    private RegisterList registerList;

    PhysicalRegister rax = PhysicalRegister.rax;
    PhysicalRegister rbx = PhysicalRegister.rbx;
    PhysicalRegister rcx = PhysicalRegister.rcx;
    PhysicalRegister rdx = PhysicalRegister.rdx;
    PhysicalRegister rsi = PhysicalRegister.rsi;
    PhysicalRegister rdi = PhysicalRegister.rdi;
    PhysicalRegister rbp = PhysicalRegister.rbp;
    PhysicalRegister rsp = PhysicalRegister.rsp;
    PhysicalRegister r8  = PhysicalRegister.r8;
    PhysicalRegister r9  = PhysicalRegister.r9;
    PhysicalRegister r10 = PhysicalRegister.r10;
    PhysicalRegister r11 = PhysicalRegister.r11;
    PhysicalRegister r12 = PhysicalRegister.r12;
    PhysicalRegister r13 = PhysicalRegister.r13;
    PhysicalRegister r14 = PhysicalRegister.r14;
    PhysicalRegister r15 = PhysicalRegister.r15;

    public Translator(IRGenerator irGenerator, RegisterList registerList) {
        this.irGenerator = irGenerator;
        this.registerList = registerList;
    }

    private int min(int a, int b) {
        return a < b? a: b;
    }

    private Operand prepare(Operand operand, PhysicalRegister tmp) {
        if (operand instanceof VirtualRegister)
            return new Address(rbp, null, new Immediate(-((((VirtualRegister) operand).cnt) * 8)));
        else if (operand instanceof Address) {
            Operand base = prepare(((Address) operand).base, null);
            Operand index = prepare(((Address) operand).scaledOffset, null);

            if (base instanceof Address || index instanceof Address) {
                if (index != null) {
                    list.add("\tmov\t" + tmp.name + ", " + index.toNASM() + "\n");
                    list.add("\tshl\t" + tmp.name + ", " + 3 + "\n");
                    list.add("\tadd\t" + tmp.name + ", " + base.toNASM() +"\n");
                } else list.add("\tmov\t" + tmp.name + ", " + base.toNASM() +"\n");
                return new Address(tmp, null, ((Address) operand).offset);
            } else return operand;
        }
        else return operand;
    }
    private void uvisit(IRInstruction ins) { ins.accept(this);}

    private void enterFunc() {
//        offsetMapStack.push(new HashMap<String, Integer>());
//        curOffsetMap = offsetMapStack.peek();

        list.add(curFunc.internalName() + ":\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");

        int size = min(6, curFunc.params().size());
        for (int i = 0; i < size; ++i) {
            list.add("\tmov     qword [rbp-" + (i + 1) * 8 + "], " + registerList.paramRegs.get(i).toNASM() + "\n");
        }

        offset = curFunc.virtualRegisterCnt * 8;
        list.add("\tsub     rsp, " + offset + "\n");
        if (curFunc.name().equals("main")) return;
        for (int i = 1; i < 6; ++i)
            list.add("\tpush     " + registerList.calleeSavedRegs.get(i).toNASM()+"\n");

    }

    private void exitFunc() {
        // Done in "return"
    }

    public void translate() {
        list.add("global main\n\n");
        list.add("extern puts\n");
        list.add("extern getchar\n");
        list.add("extern putchar\n");
        list.add("extern sprintf\n");
        list.add("extern __stack_chk_fail\n");
        list.add("extern malloc\n");
        list.add("extern printf\n");
        list.add("extern strlen\n");
        list.add("extern memcpy\n");
        list.add("extern scanf\n");

        list.add("section .data\n");

        Map<String, String> staticStrs = irGenerator.ast.constantTable.strs;
        for (String key : staticStrs.keySet()) {
            String name = staticStrs.get(key);
            list.add("\tdq\t" + key.length() + "\n" + name + ":\n\tdb\t");
            for (int i = 0; i < key.length(); ++i) list.add(Integer.toString((int)key.charAt(i)) + ", ");
            list.add("0\n");
        }
//            for (String u : rt.SC) {
//                ans.append("\tdq\t" + u.length() + "\nstr__" + Integer.toString(cc++) + ":\n\tdb\t");
//                for (int i = 0; i < u.length(); ++i) ans.append(Integer.toString((int)u.charAt(i)) + ", ");
//                ans.append("0\n");
//            }
//            for (String u : rt.SC2) {
//                ans.append("\tdq\t500" + "\n" + u + ":\n\tdb\t");
//                for (int i = 0; i < 500; ++i) ans.append(Integer.toString(0) + ", ");
//                ans.append("0\n");
//            }
//            for (VarDef u : rt.GV) ans.append(u.name + ":\n\tdq\t0\n");
        list.add("intbuffer:\n");
        list.add("\tdq 0\n");
        list.add("format1:\n");
        list.add("\tdb\"%lld\",0\n");
        list.add("format2:\n");
        list.add("\tdb\"%s\",0\n\n");
        list.add("section .bss\n");
        list.add("stringbuffer:\n");
        list.add("\tresb 256\n");
        list.add("section .text\n");
        for (FunctionEntity func : irGenerator.funcs) {
            curFunc = func;
            enterFunc();
            for (IRInstruction ins : curFunc.irInstructions)
                uvisit(ins);
        }
        Builtin();
    }

    public void visit(Assign ins) {
        if (ins.left.toNASM() == ins.right.toNASM()) return;

        Operand l = prepare(ins.left, r11);
        Operand r = prepare(ins.right, r12);

        if (!(l instanceof PhysicalRegister) && !(r instanceof PhysicalRegister)) {
            if (r instanceof Str)
                list.add("\tmov     rax, qword " + r.toNASM() +"\n");
            else
                list.add("\tmov     rax, " + r.toNASM() +"\n");
            r = rax;
        }
        list.add("\tmov     " + l.toNASM() + ", " + r.toNASM()+"\n");

    }

    // Load left operator to physical register, deal with plain arith operation
    private void loadArith(Operand l, Operand r, String op, PhysicalRegister reg) {
        if (!(l instanceof PhysicalRegister))
            list.add("\tmov     " + reg.toNASM() + ", " + l.toNASM() +"\n");

        list.add("\t" + op + "     rax" + ", " + r.toNASM() +"\n");

        if (!(l instanceof PhysicalRegister))
            list.add("\tmov     " + l.toNASM() + ", " + reg.toNASM() +"\n");
    }

    private void loadDiv(Operand l, Operand r, String op, boolean isDiv) {
        boolean changel = false, changeR = false;
        PhysicalRegister tmpl = null, tmpr = null;
        if (r instanceof Immediate) {
            list.add("\tmov     r8, " + r.toNASM() + "\n");
            r = r8;
        }
        if (r.toNASM().equals("rax") || r.toNASM().equals("rdx")) {
            System.err.println("loadDiv meets right operation in rax");
            list.add("\tmov     r8, rax\n");
            tmpr = (PhysicalRegister) r;
            r = r8;
            changeR = true;
        }


        if (!l.toNASM().equals("rax")) {
            changel = true;
            if (l instanceof PhysicalRegister){
                tmpl = (PhysicalRegister) l;
            }
            list.add("\tmov     rax, " + l.toNASM() +"\n");
        }
        list.add("\tcqo\n");

        list.add("\t" + op + "     " + r.toNASM() +"\n");

        if (isDiv && changel)
            list.add("\tmov     " + l.toNASM() + ", rax\n");
        else if (!l.toNASM().equals("rdx"))
            list.add("\tmov     " + l.toNASM() + ", rdx\n");

        if (changeR) {
            list.add("\tmov     " + tmpr.toNASM() +", r8\n");
            r = rax;
        }
    }

    private void loadCmp(Operand l, Operand r, String op) {
        if (!l.toNASM().equals("rcx"))
            list.add("\tmov     rcx, " + l.toNASM() + "\n");
        list.add("\tcmp     rcx, " + r.toNASM() + "\n");
        list.add("\t" + op + "      cl\n\tmovzx   rcx, cl\n");
        list.add("\tmov     " + l.toNASM() + ", rcx\n");
    }

    private void loadShift(Operand l, Operand r, String op) {
        if (!(r instanceof Immediate)) {
            list.add("\tmov     rax, " + r.toNASM() + "\n");
            list.add("\tmov     ecx, eax\n");
            list.add("\t" + op + "     " + l.toNASM() + ", cl\n");
        }
        else
            list.add("\t" + op + "\t" + l.toNASM() + ", " + r.toNASM() +"\n");
    }

    public void visit(Binop ins) {
        Operand l = prepare(ins.left,  r10);
        Operand r = prepare(ins.right, r11);

//        switch (x.op) {
//            case "+" : A(x.dest, x.lhs, x.rhs, "add"); break;
//            case "-" : A(x.dest, x.lhs, x.rhs, "sub"); break;
//            case "*" : A(x.dest, x.lhs, x.rhs, "imul"); break;
//            case "&" : A(x.dest, x.lhs, x.rhs, "and"); break;
//            case "|" : A(x.dest, x.lhs, x.rhs, "or"); break;
//            case "^" : A(x.dest, x.lhs, x.rhs, "xor"); break;
//
//            case ">" : B(x.dest, x.lhs, x.rhs, "setg"); break;
//            case "<" : B(x.dest, x.lhs, x.rhs, "setl"); break;
//            case ">=" : B(x.dest, x.lhs, x.rhs, "setge"); break;
//            case "<=" : B(x.dest, x.lhs, x.rhs, "setle"); break;
//            case "==" : B(x.dest, x.lhs, x.rhs, "sete"); break;
//            case "!=" : B(x.dest, x.lhs, x.rhs, "setne"); break;
//
//            case "<<" : C(x.dest, x.lhs, x.rhs, "shl"); break;
//            case ">>" : C(x.dest, x.lhs, x.rhs, "shr"); break;
//
//            case "/" : D(x.dest, x.lhs, x.rhs, "div"); break;
//            case "%" : D(x.dest, x.lhs, x.rhs, "mod"); break;
//        }

        switch (ins.operator) {
            case ADD: loadArith(l, r, "add", rax); break;
            case SUB: loadArith(l, r, "sub", rax); break;
            case MUL: loadArith(l, r, "imul", rax); break;
            case AND: loadArith(l, r, "and", rax); break;
            case OR: loadArith(l, r, "or", rax); break;
            case XOR: loadArith(l, r, "xor", rax); break;

            case DIV: loadDiv(l, r, "idiv", true); break;
            case MOD: loadDiv(l, r, "idiv", false); break;

            case NE: loadCmp(l, r, "setne"); break;
            case EQ: loadCmp(l, r, "sete"); break;
            case GT: loadCmp(l, r, "setg"); break;
            case LT: loadCmp(l, r, "setl"); break;
            case GE: loadCmp(l, r, "setge"); break;
            case LE: loadCmp(l, r, "setle"); break;

            case LSH: loadShift(l, r, "shl"); break;
            case RSH: loadShift(l, r, "shr"); break;
        }

    }

    public void visit(Call ins) {
        list.add("\tcall    " + ins.func.internalName() +"\n");
    }

    public void visit(Cjump ins) {
        Operand l = prepare(((Binop) ins.cond).left, r10);
        Operand r = prepare(((Binop) ins.cond).right, r11);
        if (l instanceof Immediate) {
            Operand tmp = r;
            r = l;
            l = tmp;
        }
        if (l instanceof Address && r instanceof Address) {
            list.add("\tmov     rcx, " + l.toNASM() + "\n");
            l = rcx;
        }
        list.add("\tcmp     " + l.toNASM() + ", " + r.toNASM() +"\n");

        switch (((Binop) ins.cond).operator) {
            case NE: {
                if (ins.thenLabel != null) list.add("\tjne     " + ins.thenLabel.toNASM() +"\n");
                if (ins.elseLabel != null) list.add("\tje     " + ins.elseLabel.toNASM() +"\n");
                break;
            }
            case LT: {
                if (ins.thenLabel != null) list.add("\tjl     " + ins.thenLabel.toNASM() +"\n");
                if (ins.elseLabel != null) list.add("\tjnl     " + ins.elseLabel.toNASM() +"\n");
                break;
            }
            case LE: {
                if (ins.thenLabel != null) list.add("\tjle     " + ins.thenLabel.toNASM() +"\n");
                if (ins.elseLabel != null) list.add("\tjnle     " + ins.elseLabel.toNASM() +"\n");
                break;
            }
            case GT:{
                if (ins.thenLabel != null) list.add("\tjg     " + ins.thenLabel.toNASM() +"\n");
                if (ins.elseLabel != null) list.add("\tjng     " + ins.elseLabel.toNASM() +"\n");
                break;
            }
            case GE: {
                if (ins.thenLabel != null) list.add("\tjge     " + ins.thenLabel.toNASM() +"\n");
                if (ins.elseLabel != null) list.add("\tjl     " + ins.elseLabel.toNASM() +"\n");
                break;
            }
            case EQ: {
                if (ins.thenLabel != null) list.add("\tje     " + ins.thenLabel.toNASM() +"\n");
                if (ins.elseLabel != null) list.add("\tjne     " + ins.elseLabel.toNASM() +"\n");
                break;
            }

        }
    }
    public void visit(Jump ins) {
        list.add("\tjmp    "+ins.target.toNASM() + "\n");
    }
    public void visit(Label ins) {
        list.add(ins.toNASM() + ":\n");
    }
    public void visit(Return ins) {
        if (!curFunc.name().equals("main")) {
            for (int i = 5; i >= 1; --i)
                list.add("\tpop     " + registerList.calleeSavedRegs.get(i).toNASM()+"\n");
        }
        list.add("\tleave\n");
        list.add("\tret\n");
    }
    public void visit(Uniop ins) {
        Operand operand = prepare(ins.operand, rcx);
        switch (ins.operator) {
            case NEG:  list.add("\tnot     " + operand.toNASM() + "\n");
        }
    }
    public void Builtin() {
        //toString
        list.add("toString:\n");
        list.add("\tpush     rbp\n");
        list.add("\tmov      rbp,rsp\n");
        list.add("\tmov      rdx,rdi\n");
        list.add("\tmov      rax,0\n");
        list.add("\tmov      rdi,stringbuffer\n");
        list.add("\tmov      rsi,format1\n");
        list.add("\tcall     sprintf\n");
        list.add("\tmov      rdi,stringbuffer\n");
        list.add("\tcall     transtring\n");
        list.add("\tmov      rsp,rbp\n");
        list.add("\tpop      rbp\n");
        list.add("\tret\n");


        //println
        list.add("println:\n");
        list.add("\tcall puts\n");
        list.add("\tret\n\n");

        //array.size
        list.add("array.size:\n");
        list.add("\tmov\trax, qword [rdi - 8]\n");
        list.add("\tret\n\n");

        //string.add
        list.add("string.add:\n");
        list.add("\tpush rbp\n");
        list.add("\tmov rbp,rsp\n");
        list.add("\tpush rsi\n");
        list.add("\tmov rsi,rdi\n");
        list.add("\tmov rdi,stringbuffer\n");
        list.add("\tmov rdx,[rsi-8]\n");
        list.add("\tpush rdx\n");
        list.add("\tcall memcpy\n");
        list.add("\tpop rdi\n");
        list.add("\tpop rsi\n");
        list.add("\tadd rdi,stringbuffer\n");
        list.add("\tmov rdx,[rsi-8]\n");
        list.add("\tadd rdx,1\n");
        list.add("\tcall memcpy\n");
        list.add("\tmov rdi,stringbuffer\n");
        list.add("\tcall transtring\n");
        list.add("\tmov rsp,rbp\n");
        list.add("\tpop rbp\n");
        list.add("\tret\n");




        //getInt
        list.add("getInt:\n");
        list.add("\tpush rbp\n");
        list.add("\tmov rbp,rsp\n");
        list.add("\tmov rax,0\n");
        list.add("\tmov rdi,format1\n");
        list.add("\tmov rsi,intbuffer\n");
        list.add("\tcall scanf\n");
        list.add("\tmov rax,[intbuffer]\n");
        list.add("\tmov rsp,rbp\n");
        list.add("\tpop rbp\n");
        list.add("\tret\n");

        //string.length
        list.add("string.length:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     dword [rbp-4H], 0\n");
        list.add("\tjmp     Llen_002\n");
        list.add("Llen_001:  add     dword [rbp-4H], 1\n");
        list.add("Llen_002:  mov     eax, dword [rbp-4H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjnz     Llen_001\n");
        list.add("\tmov     eax, dword [rbp-4H]\n");
        list.add("\tpop     rbp\n");
        list.add("\tret\n");

        //string.ord
        list.add("string.ord:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     dword [rbp-1CH], esi\n");
        list.add("\tmov     eax, dword [rbp-1CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tmovsx   eax, al\n");
        list.add("\tmov     dword [rbp-4H], eax\n");
        list.add("\tmov     eax, dword [rbp-4H]\n");
        list.add("\tpop     rbp\n");
        list.add("\tret\n");

        //print
        list.add("print:\n");
        list.add("\tpush rbp\n");
        list.add("\tmov rbp,rsp\n");
        list.add("\tmov rax,0\n");
        list.add("\tmov rsi,rdi\n");
        list.add("\tmov rdi,format2\n");
        list.add("\tcall printf\n");
        list.add("\tmov rsp,rbp\n");
        list.add("\tpop rbp\n");
        list.add("\tret\n");

        //getString
        list.add("transtring:\n");
        list.add("\n");
        list.add("\tpush rbp\n");
        list.add("\tmov rbp,rsp\n");
        list.add("\tcall strlen\n");
        list.add("\tpush rdi\n");
        list.add("\tmov rdi,rax\n");
        list.add("\tpush rdi\n");
        list.add("\tadd rdi,9\n");
        list.add("\tcall malloc\n");
        list.add("\tpop rdi\n");
        list.add("\tmov [rax],rdi\n");
        list.add("\tadd rax,8\n");
        list.add("\tmov rdx,rdi\n");
        list.add("\tadd rdx,1\n");
        list.add("\tmov rdi,rax\n");
        list.add("\tpop rsi\n");
        list.add("\tsub rsp,8\n");
        list.add("\tpush rax\n");
        list.add("\tcall memcpy\n");
        list.add("\tpop rax\n");
        list.add("\tmov rsp,rbp\n");
        list.add("\tpop rbp\n");
        list.add("\tret\n");
        list.add("\n");
        list.add("getString:\n");
        list.add("\n");
        list.add("\tpush rbp\n");
        list.add("\tmov rbp,rsp\n");
        list.add("\tmov rax,0\n");
        list.add("\tmov rdi,format2\n");
        list.add("\tmov rsi,stringbuffer\n");
        list.add("\tcall scanf\n");
        list.add("\tmov rdi,stringbuffer\n");
        list.add("\tcall transtring\n");
        list.add("\tmov rsp,rbp\n");
        list.add("\tpop rbp\n");
        list.add("\tret\n");
        list.add("\n");

        //string.substring
        list.add("string.substring:\n");
        list.add("\tpush rbp\n");
        list.add("\tmov rbp,rsp\n");
        list.add("\tpush rdi\n");
        list.add("\tpush rsi\n");
        list.add("\tmov rdi,rdx\n");
        list.add("\tsub rdi,rsi\n");
        list.add("\tadd rdi,1\n");
        list.add("\tpush rdi\n");
        list.add("\tadd rdi,9\n");
        list.add("\tcall malloc\n");
        list.add("\tpop rdx\n");
        list.add("\tmov [rax],rdx\n");
        list.add("\tadd rax,8\n");
        list.add("\tpop rsi\n");
        list.add("\tpop rdi\n");
        list.add("\tadd rsi,rdi\n");
        list.add("\tmov rdi,rax\n");
        list.add("\tpush rdx\n");
        list.add("\tpush rax\n");
        list.add("\tcall memcpy\n");
        list.add("\tpop rax\n");
        list.add("\tpop rdx\n");
        list.add("\tmov qword[rax+rdx],0\n");
        list.add("\tmov rsp,rbp\n");
        list.add("\tpop rbp\n");
        list.add("\tret\n");
        list.add("\n");

        //string.parseInt
        list.add("string.parseInt:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tsub     rsp, 32\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     edi, 256\n");
        list.add("\tcall    malloc\n");
        list.add("\tmov     qword [rbp-8H], rax\n");
        list.add("\tmov     dword [rbp-10H], 0\n");
        list.add("\tmov     dword [rbp-0CH], 0\n");
        list.add("\tjmp     Lpar_002\n");
        list.add("Lpar_001:  add     dword [rbp-10H], 1\n");
        list.add("Lpar_002:  mov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Lpar_004\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Lpar_001\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Lpar_001\n");
        list.add("\tjmp     Lpar_004\n");
        list.add("Lpar_003:  mov     edx, dword [rbp-0CH]\n");
        list.add("\tmov     eax, edx\n");
        list.add("\tshl     eax, 2\n");
        list.add("\tadd     eax, edx\n");
        list.add("\tadd     eax, eax\n");
        list.add("\tmov     ecx, eax\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tmovsx   eax, al\n");
        list.add("\tadd     eax, ecx\n");
        list.add("\tsub     eax, 48\n");
        list.add("\tmov     dword [rbp-0CH], eax\n");
        list.add("\tadd     dword [rbp-10H], 1\n");
        list.add("Lpar_004:  mov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 47\n");
        list.add("\tjle     Lpar_005\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjle     Lpar_003\n");
        list.add("Lpar_005:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tleave\n");
        list.add("\tret\n");

        //string.eq
        list.add("\tstring.eq:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     qword [rbp-20H], rsi\n");
        list.add("\tmov     dword [rbp-0CH], 0\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tjmp     Leq_002\n");
        list.add("Leq_001:  add     dword [rbp-0CH], 1\n");
        list.add("Leq_002:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Leq_004\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Leq_001\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Leq_001\n");
        list.add("\tjmp     Leq_004\n");
        list.add("Leq_003:  add     dword [rbp-8H], 1\n");
        list.add("Leq_004:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Leq_005\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Leq_003\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Leq_003\n");
        list.add("Leq_005:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tcmp     eax, dword [rbp-8H]\n");
        list.add("\tjz      Leq_006\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Leq_010\n");
        list.add("Leq_006:  mov     dword [rbp-4H], 0\n");
        list.add("\tmov     dword [rbp-4H], 0\n");
        list.add("\tjmp     Leq_009\n");
        list.add("Leq_007:  mov     eax, dword [rbp-4H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-4H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjz      Leq_008\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Leq_010\n");
        list.add("Leq_008:  add     dword [rbp-4H], 1\n");
        list.add("Leq_009:  mov     eax, dword [rbp-4H]\n");
        list.add("\tcmp     eax, dword [rbp-0CH]\n");
        list.add("\tjl      Leq_007\n");
        list.add("\tmov     eax, 1\n");
        list.add("Leq_010:  pop     rbp\n");
        list.add("\tret\n");

        //string.s
        list.add("string.lt:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     qword [rbp-20H], rsi\n");
        list.add("\tmov     dword [rbp-10H], 0\n");
        list.add("\tmov     dword [rbp-0CH], 0\n");
        list.add("\tjmp     Ll_012\n");
        list.add("Ll_011:  add     dword [rbp-10H], 1\n");
        list.add("Ll_012:  mov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Ll_014\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Ll_011\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Ll_011\n");
        list.add("\tjmp     Ll_014\n");
        list.add("Ll_013:  add     dword [rbp-0CH], 1\n");
        list.add("Ll_014:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Ll_015\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Ll_013\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Ll_013\n");
        list.add("Ll_015:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tcmp     dword [rbp-10H], eax\n");
        list.add("\tcmovle  eax, dword [rbp-10H]\n");
        list.add("\tmov     dword [rbp-4H], eax\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tjmp     Ll_019\n");
        list.add("Ll_016:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjge     Ll_017\n");
        list.add("\tmov     eax, 1\n");
        list.add("\tjmp     Ll_021\n");
        list.add("Ll_017:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjle     Ll_018\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Ll_021\n");
        list.add("Ll_018:  add     dword [rbp-8H], 1\n");
        list.add("Ll_019:  mov     eax, dword [rbp-8H]\n");
        list.add("\tcmp     eax, dword [rbp-4H]\n");
        list.add("\tjl      Ll_016\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tcmp     eax, dword [rbp-0CH]\n");
        list.add("\tjl      Ll_020\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Ll_021\n");
        list.add("Ll_020:  mov     eax, 1\n");
        list.add("Ll_021:  pop     rbp\n");
        list.add("\tret\n");

        //string.g
        list.add("string.gt:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     qword [rbp-20H], rsi\n");
        list.add("\tmov     dword [rbp-10H], 0\n");
        list.add("\tmov     dword [rbp-0CH], 0\n");
        list.add("\tjmp     Lg_023\n");
        list.add("Lg_022:  add     dword [rbp-10H], 1\n");
        list.add("Lg_023:  mov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Lg_025\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Lg_022\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Lg_022\n");
        list.add("\tjmp     Lg_025\n");
        list.add("Lg_024:  add     dword [rbp-0CH], 1\n");
        list.add("Lg_025:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Lg_026\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Lg_024\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Lg_024\n");
        list.add("Lg_026:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tcmp     dword [rbp-10H], eax\n");
        list.add("\tcmovle  eax, dword [rbp-10H]\n");
        list.add("\tmov     dword [rbp-4H], eax\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tjmp     Lg_030\n");
        list.add("Lg_027:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjle     Lg_028\n");
        list.add("\tmov     eax, 1\n");
        list.add("\tjmp     Lg_032\n");
        list.add("Lg_028:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjge     Lg_029\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Lg_032\n");
        list.add("Lg_029:  add     dword [rbp-8H], 1\n");
        list.add("Lg_030:  mov     eax, dword [rbp-8H]\n");
        list.add("\tcmp     eax, dword [rbp-4H]\n");
        list.add("\tjl      Lg_027\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tcmp     eax, dword [rbp-0CH]\n");
        list.add("\tjg      Lg_031\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Lg_032\n");
        list.add("Lg_031:  mov     eax, 1\n");
        list.add("Lg_032:  pop     rbp\n");
        list.add("\tret\n");

        //string.le
        list.add("\tstring.le:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     qword [rbp-20H], rsi\n");
        list.add("\tmov     dword [rbp-10H], 0\n");
        list.add("\tmov     dword [rbp-0CH], 0\n");
        list.add("\tjmp     Llege_002\n");
        list.add("Llege_001:  add     dword [rbp-10H], 1\n");
        list.add("Llege_002:  mov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Llege_004\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Llege_001\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Llege_001\n");
        list.add("\tjmp     Llege_004\n");
        list.add("Llege_003:  add     dword [rbp-0CH], 1\n");
        list.add("Llege_004:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Llege_005\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Llege_003\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Llege_003\n");
        list.add("Llege_005:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tcmp     dword [rbp-10H], eax\n");
        list.add("\tcmovle  eax, dword [rbp-10H]\n");
        list.add("\tmov     dword [rbp-4H], eax\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tjmp     Llege_009\n");
        list.add("Llege_006:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjge     Llege_007\n");
        list.add("\tmov     eax, 1\n");
        list.add("\tjmp     Llege_011\n");
        list.add("Llege_007:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjle     Llege_008\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Llege_011\n");
        list.add("Llege_008:  add     dword [rbp-8H], 1\n");
        list.add("Llege_009:  mov     eax, dword [rbp-8H]\n");
        list.add("\tcmp     eax, dword [rbp-4H]\n");
        list.add("\tjl      Llege_006\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tcmp     eax, dword [rbp-0CH]\n");
        list.add("\tjle     Llege_010\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Llege_011\n");
        list.add("\tLlege_010:  mov     eax, 1\n");
        list.add("\tLlege_011:  pop     rbp\n");
        list.add("\tret\n");

        //string.ge
        list.add("\tstring.ge:\n");
        list.add("\tpush    rbp\n");
        list.add("\tmov     rbp, rsp\n");
        list.add("\tmov     qword [rbp-18H], rdi\n");
        list.add("\tmov     qword [rbp-20H], rsi\n");
        list.add("\tmov     dword [rbp-10H], 0\n");
        list.add("\tmov     dword [rbp-0CH], 0\n");
        list.add("\tjmp     Llege_013\n");
        list.add("Llege_012:  add     dword [rbp-10H], 1\n");
        list.add("Llege_013:  mov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Llege_015\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Llege_012\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Llege_012\n");
        list.add("\tjmp     Llege_015\n");
        list.add("Llege_014:  add     dword [rbp-0CH], 1\n");
        list.add("Llege_015:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjz      Llege_016\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\ttest    al, al\n");
        list.add("\tjs      Llege_014\n");
        list.add("\tmov     eax, dword [rbp-0CH]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     al, 57\n");
        list.add("\tjg      Llege_014\n");
        list.add("Llege_016:  mov     eax, dword [rbp-0CH]\n");
        list.add("\tcmp     dword [rbp-10H], eax\n");
        list.add("\tcmovle  eax, dword [rbp-10H]\n");
        list.add("\tmov     dword [rbp-4H], eax\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tmov     dword [rbp-8H], 0\n");
        list.add("\tjmp     Llege_020\n");
        list.add("Llege_017:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjle     Llege_018\n");
        list.add("\tmov     eax, 1\n");
        list.add("\tjmp     Llege_022\n");
        list.add("Llege_018:  mov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rdx, eax\n");
        list.add("\tmov     rax, qword [rbp-18H]\n");
        list.add("\tadd     rax, rdx\n");
        list.add("\tmovzx   edx, byte [rax]\n");
        list.add("\tmov     eax, dword [rbp-8H]\n");
        list.add("\tmovsxd  rcx, eax\n");
        list.add("\tmov     rax, qword [rbp-20H]\n");
        list.add("\tadd     rax, rcx\n");
        list.add("\tmovzx   eax, byte [rax]\n");
        list.add("\tcmp     dl, al\n");
        list.add("\tjge     Llege_019\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Llege_022\n");
        list.add("Llege_019:  add     dword [rbp-8H], 1\n");
        list.add("Llege_020:  mov     eax, dword [rbp-8H]\n");
        list.add("\tcmp     eax, dword [rbp-4H]\n");
        list.add("\tjl      Llege_017\n");
        list.add("\tmov     eax, dword [rbp-10H]\n");
        list.add("\tcmp     eax, dword [rbp-0CH]\n");
        list.add("\tjge     Llege_021\n");
        list.add("\tmov     eax, 0\n");
        list.add("\tjmp     Llege_022\n");
        list.add("Llege_021:  mov     eax, 1\n");
        list.add("Llege_022:  pop     rbp\n");
        list.add("\tret\n");
    }
}
