package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.NASM.Operand.Operand;
import com.vegw.compiler.NASM.Operand.VirtualRegister;

public class Mod extends Arith {
    public Mod(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
