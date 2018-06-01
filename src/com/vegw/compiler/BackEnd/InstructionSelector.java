package com.vegw.compiler.BackEnd;

import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.IR.LinearIR.*;
import com.vegw.compiler.NASM.Instruction.*;
import com.vegw.compiler.NASM.Operand.Address;
import com.vegw.compiler.NASM.Operand.Immediate;
import com.vegw.compiler.NASM.Operand.Operand;
import com.vegw.compiler.NASM.Operand.VirtualRegister;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InstructionSelector {
    private int virtualRegisterCnt;
    private IRGenerator irGenerator;
    private Map<Entity, VirtualRegister> vrMap;
    private Map<String, AsmLabel> lbMap;
    private List<IRInstruction> stmts;
    private List<Instruction> insts;

    public InstructionSelector(IRGenerator irGenerator) {
        virtualRegisterCnt = 0;
        this.irGenerator = irGenerator;
        this.vrMap = new HashMap<Entity, VirtualRegister>();
        this.lbMap = new HashMap<String, AsmLabel>();
        this.stmts = irGenerator.getStmts();
        this.insts = new LinkedList<Instruction>();
    }

    public void instructionSelect() {
        for (IRInstruction stmt : stmts)
            uvisit(stmt);
    }

    private AsmLabel getLabel(String name) {
        AsmLabel label = lbMap.get(name);
        if (label == null) {
            label = new AsmLabel(name);
            lbMap.put(name, label);
        }
        return label;
    }

    private Operand uvisit(IRInstruction stmt) { return stmt.accept(this); }

    public Operand visit(Addr ins) {
        Entity entity = ins.entity;
        VirtualRegister vr = vrMap.get(entity);
        if (vr == null) throw new InternalError("Addressing unknown virtual register");
        return vr;
    }

    public Operand visit(Assign ins) {
        Operand src;
        Operand dst;
        VirtualRegister vr;
        dst = uvisit(ins.left);
        src = uvisit(ins.right);

        if (dst instanceof Address && src instanceof Address) {
            vr = new VirtualRegister(virtualRegisterCnt++);
            insts.add(new Mov(vr, src));
            src = vr;
        }
        if (src != null)
            insts.add(new Mov(dst, src));

        return null;
    }

    public Operand visit(Binop ins) {
        VirtualRegister vrl, vrr;
        Operand left = uvisit(ins.left);
        Operand right = uvisit(ins.right);

        if (left instanceof Address || left instanceof VirtualRegister) {
            vrl = new VirtualRegister(virtualRegisterCnt++);
            insts.add(new Mov(vrl, left));
            left = vrl;
        }

        switch (ins.operator) {
            case ADD: insts.add(new Add((VirtualRegister) left, right)); break;
            case SUB: insts.add(new Sub((VirtualRegister) left, right)); break;
            case MUL: insts.add(new Mul((VirtualRegister) left, right)); break;
            case DIV: insts.add(new Div((VirtualRegister) left, right)); break;
            case MOD: insts.add(new Mod((VirtualRegister) left, right)); break;
            case AND: insts.add(new And((VirtualRegister) left, right)); break;
            case OR:  insts.add(new Or((VirtualRegister) left, right));  break;
            case XOR: insts.add(new Xor((VirtualRegister) left, right)); break;
            case LSH: insts.add(new Sal((VirtualRegister) left, right)); break;
            case RSH: insts.add(new Sar((VirtualRegister) left, right)); break;
            case NE:  insts.add(new Cmp(Cmp.Rel.NE, (VirtualRegister) left, right)); break;
            case EQ:  insts.add(new Cmp(Cmp.Rel.EQ, (VirtualRegister) left, right)); break;
            case LT:  insts.add(new Cmp(Cmp.Rel.LT, (VirtualRegister) left, right)); break;
            case GT:  insts.add(new Cmp(Cmp.Rel.GT, (VirtualRegister) left, right)); break;
            case LE:  insts.add(new Cmp(Cmp.Rel.LE, (VirtualRegister) left, right)); break;
            case GE:  insts.add(new Cmp(Cmp.Rel.LT, (VirtualRegister) left, right)); break;
            default: throw new InternalError("Unknown binop");
        }
        return left;
    }

    public Operand visit(Call ins) {
        int a = 4;
        int b = 3 + 3;
        a = a >> b;
        return null;
    }

    public Operand visit(Cjump ins) {
        Operand cond = uvisit(ins.cond);
        if (ins.cond instanceof Imme) {
            if (((Immediate) cond).value == 1 && ins.thenLabel != null)
                insts.add(new Jmp(getLabel(ins.thenLabel.name())));
            else if (((Immediate) cond).value == 0 && ins.elseLabel != null)
                insts.add(new Jmp(getLabel(ins.elseLabel.name())));
            return null;
        }
        else if (ins.cond instanceof Binop) {
            switch (((Binop) ins.cond).operator) {
                case NE: {
                    VirtualRegister c = (VirtualRegister) cond;

                }
                case EQ:
                case GT:
                case LT:
                case GE:
                case LE:
                    default: throw new InternalError("Unexpected binop in cjump");
            }
//            insts.add(new Cjmp(""))
//            switch ()
        }
//            throw new InternalError("Short-circuit fails");
//        if (ins.cond instanceof Uniop)
        return null;
    }

    public Immediate visit(Imme ins) {
        return new Immediate(ins.value);
    }

    public Operand visit(Jump ins) {
        insts.add(new Jmp(getLabel(ins.target.name())));
        return null;
    }

    public Operand visit(Label ins) {
        insts.add(getLabel(ins.name()));
        return null;
    }

    public Operand visit(Mem ins) {
        Operand base = uvisit(ins.base);
        if (base instanceof Address) {
            VirtualRegister vr = new VirtualRegister(virtualRegisterCnt++);
            insts.add(new Lea(vr, (Address) base));
            base = vr;
        }

        Operand scaledOffset = uvisit(ins.scaledOffset);
        if (scaledOffset instanceof Address) {
            VirtualRegister vr = new VirtualRegister(virtualRegisterCnt++);
            insts.add(new Lea(vr, (Address) scaledOffset));
            base = vr;
        }

        Immediate offset = visit(ins.offset);
        return new Address((VirtualRegister) base, (VirtualRegister) scaledOffset, offset);
    }

    public Operand visit(Return ins) {
    return null;
    }

    public Immediate visit(Str ins) {
        return new Immediate(ins.hashCode());
    }

    public Operand visit(Uniop ins) {
        Operand operand = uvisit(ins.expr);
        VirtualRegister vr = new VirtualRegister(virtualRegisterCnt++);
        insts.add(new Mov(vr, operand));
        operand = vr;

        VirtualRegister res = new VirtualRegister(virtualRegisterCnt++);
        switch (ins.operator) {
            case NEG: insts.add(new Neg((VirtualRegister) operand)); break;
            case BNOT: insts.add(new Not((VirtualRegister) operand)); break;
            case LNOT: insts.add(new Xor((VirtualRegister) operand, new Immediate(1))); break;
            default:
                throw new InternalError("Unknown uniop");
        }
        return res;
    }

    public VirtualRegister visit(Var ins) {
        VirtualRegister vr = vrMap.get(ins.entity);
        if (vr != null) return vr;
        else {
            vr = new VirtualRegister(virtualRegisterCnt++);
            vrMap.put(ins.entity, vr);
            return vr;
        }
    }


}
