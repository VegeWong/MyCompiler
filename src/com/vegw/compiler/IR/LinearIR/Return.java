package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;

import com.vegw.compiler.BackEnd.IRInstructionVisitor;
import com.vegw.compiler.BackEnd.Translator;

public class Return extends Expr {

//    @Override
//    public Operand accept(InstructionSelector is) { return is.visit(this); }

    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }

    @Override
    public void accept(IRInstructionVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
