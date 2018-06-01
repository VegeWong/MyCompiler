package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.NASM.Operand.Operand;
import com.vegw.compiler.NASM.Operand.VirtualRegister;

public class Cmp extends Arith {
    public enum Rel {
        NE, EQ, GE, LE, GT, LT
    }

    private Rel rel;

    public Cmp(Rel rel, VirtualRegister left, Operand right) {
        super(left, right);
        this.rel = rel;
    }
}
