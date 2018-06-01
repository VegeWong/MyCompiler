package com.vegw.compiler.NASM.Operand;

public class Address extends Operand{

    private VirtualRegister base = null;
    private VirtualRegister scaledOffset = null;
    private Immediate offset = null;

    public Address(VirtualRegister base, VirtualRegister scaledOffset, Immediate offset) {
        this.base = base;
        this.scaledOffset = scaledOffset;
        this.offset = offset;
    }


    @Override
    public String toNASM() {
        String str = base.toNASM();
        if (scaledOffset != null)
            str += "+8*" + scaledOffset.toNASM();
        if (offset != null)
            str += offset.toNASM();
        return str;
    }
}
