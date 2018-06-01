package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Operand.Operand;

public class Imme extends Expr {
    public int value;
    public Imme(int v) {
        value = v;
    }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }
}
