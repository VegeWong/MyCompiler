package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Operand.Operand;

public class Return extends Expr {
    public Expr value;
    public Return(Expr v) {
        value = v;
    }


    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }
}
