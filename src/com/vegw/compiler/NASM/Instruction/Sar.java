package com.vegw.compiler.NASM.Instruction;

import com.vegw.compiler.IR.LinearIR.Operand.Operand;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

public class Sar extends Arith {
    public Sar(VirtualRegister left, Operand right) {
        super(left, right);
    }
}
