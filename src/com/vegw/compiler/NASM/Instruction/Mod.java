package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Mod extends Arith {
    public Mod(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
