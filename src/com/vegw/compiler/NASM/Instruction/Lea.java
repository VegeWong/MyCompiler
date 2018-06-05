package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Address;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Lea extends Instruction {
    private VirtualRegister reg;
    private Address addr;

    public Lea(VirtualRegister reg, Address addr) {
        this.reg = reg;
        this.addr = addr;
    }
}
