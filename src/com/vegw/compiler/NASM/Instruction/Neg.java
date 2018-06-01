package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.NASM.Operand.VirtualRegister;

public class Neg extends Instruction {
    private VirtualRegister operand;

    public Neg(VirtualRegister operand){
        this.operand = operand;
    }

}
