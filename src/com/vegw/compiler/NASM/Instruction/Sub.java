package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Sub extends Arith {
    public Sub(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
