package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Neg extends Instruction {
    private VirtualRegister operand;

    public Neg(VirtualRegister operand){
        this.operand = operand;
    }

}
