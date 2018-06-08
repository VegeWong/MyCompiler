package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.IRInstructionVisitor;
import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.IR.LinearIR.Operand.Operand;

public class Push extends IRInstruction {
    public Operand operand;
    public Push(Operand o) { operand = o; }

    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }

    @Override
    public void accept(IRInstructionVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
