package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class And extends Arith {
    public And(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
