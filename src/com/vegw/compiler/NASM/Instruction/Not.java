package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Not extends Instruction {
    private VirtualRegister operand;

    public Not(VirtualRegister operand) {
        this.operand = operand;
    }
}
