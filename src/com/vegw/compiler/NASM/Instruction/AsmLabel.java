package com.vegw.compiler.NASM.Instruction;

public class AsmLabel extends Instruction {
    private String name;
    public AsmLabel(String name) { this.name = name; }
}
