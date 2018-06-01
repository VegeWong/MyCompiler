package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.NASM.Operand.Operand;
import com.vegw.compiler.NASM.Operand.VirtualRegister;

public class Mul extends Arith {
    public Mul(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
