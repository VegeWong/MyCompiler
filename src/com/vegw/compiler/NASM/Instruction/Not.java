package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.NASM.Operand.VirtualRegister;

public class Not extends Instruction {
    private VirtualRegister operand;

    public Not(VirtualRegister operand) {
        this.operand = operand;
    }
}
