package com.vegw.compiler.IR.LinearIR.Operand;

public class Address extends Operand{

    public Operand base = null;
    public Operand scaledOffset = null;
    public Immediate offset = null;

    public Address(Operand base, Operand scaledOffset, Immediate offset) {
        this.base = base;
        this.scaledOffset = scaledOffset;
        this.offset = offset;
    }


    @Override
    public String toNASM() {
        String str = "qword [" + base.toNASM();
        if (scaledOffset != null)
            str += "+8*" + scaledOffset.toNASM();
        if (offset != null && offset.value != 0) {
            if (offset.value > 0)
                str += "+";
            str += offset.toNASM();
        }
        str += "]";
        return str;
    }
}
