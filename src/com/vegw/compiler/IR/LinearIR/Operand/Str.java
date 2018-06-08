package com.vegw.compiler.IR.LinearIR.Operand;

public class Str extends Operand {
    public String name;
    public String value;
    public Str(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toNASM() {
        return name;
    }
}
