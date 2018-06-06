package com.vegw.compiler.IR.LinearIR.Operand;

public class VirtualRegister extends Register {
    public int cnt = -1;

    // If the virtual register is an copy of value of other virtual register before computing
    private int color = -1;

    public VirtualRegister(int cnt) { this.cnt = cnt; }

    public void setColor(int color) { this.color = color; }

    public int getColor() { return color; }

    @Override
    public String toNASM() {
        return "qword [rbp-" + cnt*8 + "]";
    }
}
