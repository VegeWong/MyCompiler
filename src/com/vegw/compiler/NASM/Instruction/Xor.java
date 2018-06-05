package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Xor extends Arith {
    public Xor(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
