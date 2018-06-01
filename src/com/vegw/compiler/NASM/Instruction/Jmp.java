package com.vegw.compiler.NASM.Instruction;

public class Jmp extends Instruction {
    private AsmLabel label;
    public Jmp(AsmLabel label) { this.label = label; }
}
