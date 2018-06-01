package com.vegw.compiler.NASM.Instruction;

public class Cjmp extends Instruction {
    private String cond;
    private AsmLabel label;
    public Cjmp(String cond, AsmLabel label) {
        this.cond = cond;
        this.label = label;
    }


}
