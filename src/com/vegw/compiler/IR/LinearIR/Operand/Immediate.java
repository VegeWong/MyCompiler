package com.vegw.compiler.IR.LinearIR.Operand;

public class Immediate extends Operand {
    // Fixed to 10-based integer && qword

    public int value;

    public Immediate(int value) { this.value = value; }


    @Override
    public String toNASM() {
        return String.valueOf(value);
    }
}
