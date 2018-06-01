package com.vegw.compiler.NASM.Operand;

public class VirtualRegister extends Operand {
    public int cnt = -1;
    private Operand value;

    // If the virtual register is an copy of value of other virtual register before computing

    private int color = -1;

    public VirtualRegister(int cnt) { this.cnt = cnt; }

    public void setValue(Operand value) { this.value = value; }

    public Operand getValue() { return value; }

    public void setColor(int color) { this.color = color; }

    public int getColor() { return color; }

    @Override
    public String toNASM() {
        throw new InternalError("Converting virtual register to NASM string");
    }
}
