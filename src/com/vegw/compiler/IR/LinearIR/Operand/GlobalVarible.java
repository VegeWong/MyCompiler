package com.vegw.compiler.IR.LinearIR.Operand;

public class GlobalVarible extends Operand {
    public String name;

    public GlobalVarible(String n) { name = "__global__" + n; }

    @Override
    public String toNASM() {
        return ("qword [" + name + "]");
    }
}
