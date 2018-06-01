package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.NASM.Operand.Address;
import com.vegw.compiler.NASM.Operand.VirtualRegister;

public class Lea extends Instruction {
    private VirtualRegister reg;
    private Address addr;

    public Lea(VirtualRegister reg, Address addr) {
        this.reg = reg;
        this.addr = addr;
    }
}
