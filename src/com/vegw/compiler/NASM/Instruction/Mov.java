package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;

public class Mov extends Instruction {
    private Operand left;
    private Operand right;

    public Mov(Operand left, Operand right) {
        this.left = left;
        this.right = right;
    }
}
