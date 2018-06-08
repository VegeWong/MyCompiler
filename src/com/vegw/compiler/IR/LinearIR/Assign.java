package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;

import com.vegw.compiler.BackEnd.IRInstructionVisitor;
import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.IR.LinearIR.Operand.Operand;

public class Assign extends Expr {
    public Operand left;
    public Operand right;

    public Assign(Operand l, Operand r) {
        left = l;
        right = r;
    }

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
