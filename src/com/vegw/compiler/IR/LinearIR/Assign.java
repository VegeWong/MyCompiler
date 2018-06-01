package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Instruction.Instruction;
import com.vegw.compiler.NASM.Operand.Operand;

public class Assign extends Expr {
    public Expr left;
    public Expr right;

    public Assign(Expr l, Expr r) {
        left = l;
        right = r;
    }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }

}
