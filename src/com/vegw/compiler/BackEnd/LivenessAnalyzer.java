package com.vegw.compiler.BackEnd;

import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.IR.LinearIR.*;
import com.vegw.compiler.IR.LinearIR.Operand.Address;
import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.PhysicalRegister;
import com.vegw.compiler.IR.LinearIR.Operand.Register;

public class LivenessAnalyzer extends IRInstructionVisitor{
    private IRGenerator irGenerator;

    public LivenessAnalyzer(IRGenerator irGenerator) {
        this.irGenerator = irGenerator;
    }

    public void analize() {
        for (FunctionEntity func : irGenerator.funcs)
            analizeFunc(func);
    }

    private void analizeFunc(FunctionEntity func) {
        for (IRInstruction ins : func.irInstructions)
            uvisit(ins);
    }
    @Override
    public void visit(Assign ins) {
        setDef(ins, ins.left);
        setUse(ins, ins.left);
        setUse(ins, ins.right);
    }

    @Override
    public void visit(Binop ins) {
        setDef(ins, ins.left);
        setUse(ins, ins.left);
        setUse(ins, ins.right);
    }

    @Override
    public void visit(Call ins) {
        int paramSize = ins.func.params().size() > 6? 6 : ins.func.params().size();
        for (int i = 0; i < paramSize; ++i)
            setUse(ins, irGenerator.registerList.paramRegs.get(i));
        setDef(ins, PhysicalRegister.rax);
    }

    @Override
    public void visit(Cjump ins) {
        return;
    }

    @Override
    public void visit(Jump ins) {
        return;
    }

    @Override
    public void visit(Label ins) {
        return;
    }

    @Override
    public void visit(Push ins) {
        setUse(ins, ins.operand);
    }

    @Override
    public void visit(Return ins) {
        return;
    }

    @Override
    public void visit(Uniop ins) {
        setUse(ins, ins.operand);
        setDef(ins, ins.operand);
    }

    private void setDef(IRInstruction ins, Operand operand) {
        if (operand instanceof Register)
            ins.addDef((Register) operand);
    }

    private void setUse(IRInstruction ins, Operand operand) {
        if (operand instanceof Register) {
            ins.addUse((Register) operand);
        }
        else if (operand instanceof Address) {
            setUse(ins, ((Address) operand).base);
            setUse(ins, ((Address) operand).scaledOffset);
        }
    }

}
