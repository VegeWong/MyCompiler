package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.NASM.Operand.Operand;
import com.vegw.compiler.NASM.Operand.VirtualRegister;

public class Sal extends Arith {
    public Sal(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
