package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Sal extends Arith {
    public Sal(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
