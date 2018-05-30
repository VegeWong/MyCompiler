package com.vegw.compiler.IR.Tree;

import com.vegw.compiler.Utils.Constants;

public class Mem extends Expr {
    protected Expr base;
    protected Expr scale = new Imme(Constants.PointerSize);
    protected Expr scaledOffset;
    protected Expr offset = new Imme(Constants.IntSize);

    public Mem(Expr base, Expr scaledOffset) {
        this.base = base;
        this.scaledOffset = scaledOffset;
    }

    public Mem(Expr base, Expr scaledOffset, Expr offset) {
        this.base = base;
        this.scaledOffset = scaledOffset;
        this.offset = offset;
    }
}
