package com.vegw.compiler.BackEnd;

import com.vegw.compiler.AST.Expr.BinaryOpNode;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.ParameterEntity;
import com.vegw.compiler.IR.LinearIR.*;
import com.vegw.compiler.IR.LinearIR.Operand.Address;
import com.vegw.compiler.IR.LinearIR.Operand.Register;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

import java.util.LinkedList;
import java.util.List;

public class RedundantElimator {
    private IRGenerator irGenerator;
    private List<IRInstruction> irs;
    private int[] ref;
    public RedundantElimator(IRGenerator irGenerator) {this.irGenerator = irGenerator;}

    public void elimate() {
        for (FunctionEntity func : irGenerator.funcs) {
            referenceCnt(func);
            elimateFunc(func);
        }
    }

    private void referenceCnt(FunctionEntity func) {
        ref = new int[func.idCnt];
        List<IRInstruction> insts = func.irInstructions;
        for (int i = 0; i < insts.size(); ++i) {
            IRInstruction inst = insts.get(i);
            if (inst instanceof Assign) {
                if (((Assign) inst).right instanceof VirtualRegister) {
                    setReg((VirtualRegister) ((Assign) inst).right);
                } else if (((Assign) inst).right instanceof Address) {
                    setAddr((Address) ((Assign) inst).right);
                }
            }
            else if (inst instanceof Binop) {
                if (((Binop) inst).left instanceof VirtualRegister)
                    setReg((VirtualRegister) ((Binop) inst).left);
                else if (((Binop) inst).left instanceof Address)
                    setAddr((Address) (((Binop) inst).left));
                if (((Binop) inst).right instanceof VirtualRegister)
                    setReg((VirtualRegister) ((Binop) inst).right);
                else if (((Binop) inst).right instanceof Address)
                    setAddr((Address) (((Binop) inst).right));
            }
            else if (inst instanceof Uniop) {
                if (((Uniop) inst).operand instanceof VirtualRegister)
                    setReg((VirtualRegister) ((Uniop) inst).operand);
                else if (((Uniop) inst).operand instanceof Address)
                    setAddr((Address) ((Uniop) inst).operand);
            }
            else if (inst instanceof Push) {
                if (((Uniop) inst).operand instanceof VirtualRegister)
                    setReg((VirtualRegister) ((Uniop) inst).operand);
            }
        }
    }

    private void setReg(VirtualRegister reg) { if (reg != null && reg.id >= 0) ref[reg.id] += 1;}

    private void setAddr(Address addr) {
        if (addr.base instanceof VirtualRegister)
            setReg((VirtualRegister) addr.base);
        if (addr.scaledOffset instanceof VirtualRegister)
            setReg((VirtualRegister) addr.scaledOffset);
    }

    private void elimateFunc(FunctionEntity func) {
        irs = new LinkedList<IRInstruction>();
        List<IRInstruction> insts = func.irInstructions;
        for (int i = 0; i < insts.size(); ++i) {
            IRInstruction p = irs.isEmpty()? null : ((LinkedList<IRInstruction>) irs).getLast();
            IRInstruction n = insts.get(i);
            if (p instanceof Assign && n instanceof Assign) {
                if (((Assign) p).left == ((Assign) n).right) {
                    if (((Assign) p).left instanceof VirtualRegister && ref[((VirtualRegister) ((Assign) p).left).id] == 2) {
                        Assign assign = new Assign(((Assign) n).left, ((Assign) p).right);
                        ((LinkedList<IRInstruction>) irs).remove(p);
                        irs.add(assign);
                        continue;
                    }
                }
            }
            irs.add(n);
        }
        func.irInstructions =  irs;
    }
}
