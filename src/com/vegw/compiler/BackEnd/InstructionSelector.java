//package com.vegw.compiler.BackEnd;
//
//import com.vegw.compiler.AST.Stmt.Def.ClassDefNode;
//import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
//import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
//import com.vegw.compiler.Entity.ClassEntity;
//import com.vegw.compiler.Entity.Entity;
//import com.vegw.compiler.Entity.FunctionEntity;
//import com.vegw.compiler.IR.LinearIR.*;
//import com.vegw.compiler.IR.LinearIR.Operand.*;
//import com.vegw.compiler.NASM.Instruction.*;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
//public class InstructionSelector {
//    private int virtualRegisterCnt;
//    private IRGenerator irGenerator;
//    private Map<String, AsmLabel> lbMap;
//    private List<Instruction> insts;
//    private FunctionEntity curFunc;
//
//    public InstructionSelector(IRGenerator irGenerator) {
//        virtualRegisterCnt = 0;
//        this.irGenerator = irGenerator;
//        this.lbMap = new HashMap<String, AsmLabel>();
//        this.insts = new LinkedList<Instruction>();
//    }
//
//    public void instructionSelect() {
//       for (DefinitionNode def : irGenerator.ast.defs) {
//           if (def instanceof FunctionDefNode) {
//               curFunc = ((FunctionDefNode) def).entity();
//               curFunc.pushAllParams();
//               for (IRInstruction irinst : curFunc.irInstructions)
//                   uvisit(irinst);
//           } else if (def instanceof ClassDefNode) {
//               for (FunctionDefNode func : ((ClassEntity) def.entity()).funcs()) {
//                   curFunc = func.entity();
//                   curFunc.pushAllParams();
//                   for (IRInstruction irinst : curFunc.irInstructions)
//                       uvisit(irinst);
//               }
//           }
//       }
//    }
//
//    private AsmLabel getLabel(String name) {
//        AsmLabel label = lbMap.get(name);
//        if (label == null) {
//            label = new AsmLabel(name);
//            lbMap.put(name, label);
//        }
//        return label;
//    }
//
//    private Operand uvisit(IRInstruction stmt) { return stmt.accept(this); }
//
//    public Operand visit(Addr ins) {
//        Entity entity = ins.entity;
//        VirtualRegister vr = curFunc.getVReg(entity);
//        if (vr == null) throw new InternalError("Addressing unknown virtual register");
//        return vr;
//    }
//
//    public Operand visit(Assign ins) {
//        Operand src;
//        Operand dst;
//        VirtualRegister vr;
//        dst = uvisit(ins.left);
//        src = uvisit(ins.right);
//
//        if (ins.right instanceof Binop) {
//            switch (((Binop) ins.right).operator){
//                case LE:
//                case GE:
//                case LT:
//                case GT:
//                case EQ:
//                case NE:
//            }
//
//        }
//        if (dst instanceof Address && src instanceof Address) {
//            vr = new VirtualRegister(virtualRegisterCnt++);
//            curFunc.addInst(new Mov(vr, src));
//            src = vr;
//        }
//        if (src != null)
//            curFunc.addInst(new Mov(dst, src));
//
//        return null;
//    }
//
//    public Operand visit(Binop ins) {
//        VirtualRegister vrl;
//        Operand left = uvisit(ins.left);
//        Operand right = uvisit(ins.right);
//
//        if (left instanceof Address || left instanceof VirtualRegister) {
//            vrl = new VirtualRegister(virtualRegisterCnt++);
//            curFunc.addInst(new Mov(vrl, left));
//            left = vrl;
//        }
//
//        switch (ins.operator) {
//            case ADD: curFunc.addInst(new Add((VirtualRegister) left, right)); break;
//            case SUB: curFunc.addInst(new Sub((VirtualRegister) left, right)); break;
//            case MUL: curFunc.addInst(new Mul((VirtualRegister) left, right)); break;
//            case DIV: curFunc.addInst(new Div((VirtualRegister) left, right)); break;
//            case MOD: curFunc.addInst(new Mod((VirtualRegister) left, right)); break;
//            case AND: curFunc.addInst(new And((VirtualRegister) left, right)); break;
//            case OR:  curFunc.addInst(new Or((VirtualRegister) left, right));  break;
//            case XOR: curFunc.addInst(new Xor((VirtualRegister) left, right)); break;
//            case LSH: curFunc.addInst(new Sal((VirtualRegister) left, right)); break;
//            case RSH: curFunc.addInst(new Sar((VirtualRegister) left, right)); break;
//            case NE:  curFunc.addInst(new Cmp(Cmp.Rel.NE, (VirtualRegister) left, right)); break;
//            case EQ:  curFunc.addInst(new Cmp(Cmp.Rel.EQ, (VirtualRegister) left, right)); break;
//            case LT:  curFunc.addInst(new Cmp(Cmp.Rel.LT, (VirtualRegister) left, right)); break;
//            case GT:  curFunc.addInst(new Cmp(Cmp.Rel.GT, (VirtualRegister) left, right)); break;
//            case LE:  curFunc.addInst(new Cmp(Cmp.Rel.LE, (VirtualRegister) left, right)); break;
//            case GE:  curFunc.addInst(new Cmp(Cmp.Rel.LT, (VirtualRegister) left, right)); break;
//            default: throw new InternalError("Unknown binop");
//        }
//        return left;
//    }
//
//    public Operand visit(Call ins) {
//        int a = 4;
//        int b = 3 + 3;
//        a = a >> b;
//        return null;
//    }
//
//    public Operand visit(Cjump ins) {
//        Operand cond = uvisit(ins.cond);
//        if (ins.cond instanceof Imme) {
//            if (((Immediate) cond).value == 1 && ins.thenLabel != null)
//                curFunc.addInst(new Jmp(getLabel(ins.thenLabel.name())));
//            else if (((Immediate) cond).value == 0 && ins.elseLabel != null)
//                curFunc.addInst(new Jmp(getLabel(ins.elseLabel.name())));
//            return null;
//        }
//        else if (ins.cond instanceof Binop) {
//            curFunc.addInst(((Binop) ins.cond).left, ((Binop) ins.cond).right);
//            switch (((Binop) ins.cond).operator) {
//                case NE: {
//                    VirtualRegister c = (VirtualRegister) cond;
//
//                }
//                case EQ:
//                case GT:
//                case LT:
//                case GE:
//                case LE:
//                    default: throw new InternalError("Unexpected binop in cjump");
//            }
////            curFunc.addInst(new Cjmp(""))
////            switch ()
//        }
////            throw new InternalError("Short-circuit fails");
////        if (ins.cond instanceof Uniop)
//        return null;
//    }
//
//    public Immediate visit(Imme ins) {
//        return new Immediate(ins.value);
//    }
//
//    public Operand visit(Jump ins) {
//        curFunc.addInst(new Jmp(getLabel(ins.target.name())));
//        return null;
//    }
//
//    public Operand visit(Label ins) {
//        curFunc.addInst(getLabel(ins.name()));
//        return null;
//    }
//
//    public Operand visit(Mem ins) {
//        Operand base = uvisit(ins.base);
//        if (base instanceof Address) {
//            VirtualRegister vr = new VirtualRegister(virtualRegisterCnt++);
//            curFunc.addInst(new Lea(vr, (Address) base));
//            base = vr;
//        }
//
//        Operand scaledOffset = uvisit(ins.scaledOffset);
//        if (scaledOffset instanceof Address) {
//            VirtualRegister vr = new VirtualRegister(virtualRegisterCnt++);
//            curFunc.addInst(new Lea(vr, (Address) scaledOffset));
//            base = vr;
//        }
//
//        Immediate offset = visit(ins.offset);
//        return new Address((VirtualRegister) base, (VirtualRegister) scaledOffset, offset);
//    }
//
//    public Operand visit(Return ins) {
//    return null;
//    }
//
//    public Immediate visit(Str ins) {
//        return new Immediate(ins.hashCode());
//    }
//
//    public Operand visit(Uniop ins) {
//        Operand operand = uvisit(ins.expr);
//        VirtualRegister vr = new VirtualRegister(virtualRegisterCnt++);
//        curFunc.addInst(new Mov(vr, operand));
//        operand = vr;
//
//        VirtualRegister res = new VirtualRegister(virtualRegisterCnt++);
//        switch (ins.operator) {
//            case NEG: curFunc.addInst(new Neg((VirtualRegister) operand)); break;
//            case BNOT: curFunc.addInst(new Not((VirtualRegister) operand)); break;
//            case LNOT: curFunc.addInst(new Xor((VirtualRegister) operand, new Immediate(1))); break;
//            default:
//                throw new InternalError("Unknown uniop");
//        }
//        return res;
//    }
//
//    public VirtualRegister visit(Var ins) {
//        return curFunc.getVReg(ins.entity);
//    }
//
//
//}
