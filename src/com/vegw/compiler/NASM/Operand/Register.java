package com.vegw.compiler.NASM.Operand;

public class Register extends Operand {
    public int num;

    private String name;
    private Operand value;

    public Register(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public void setValue(Operand value) { this.value = value; }

    public Operand getValue() { return value; }

    @Override
    public String toNASM() { return name; }
}
