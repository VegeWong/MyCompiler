package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Arith extends Instruction {
    protected VirtualRegister left;
    protected Operand right;

    public Arith(VirtualRegister left, Operand right) {
        this.left = left;
        this.right = right;
    }


}
