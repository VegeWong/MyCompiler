package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Operand.Operand;
import com.vegw.compiler.Utils.Constants;

public class Mem extends Expr {
    public Expr base;
    public Expr scale = new Imme(Constants.PointerSize);
    public Expr scaledOffset;
    public Imme offset = new Imme(Constants.IntSize);

    public Mem(Expr base, Expr scaledOffset) {
        this.base = base;
        this.scaledOffset = scaledOffset;
    }

    public Mem(Expr base, Expr scaledOffset, Imme offset) {
        this.base = base;
        this.scaledOffset = scaledOffset;
        this.offset = offset;
    }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }
}
