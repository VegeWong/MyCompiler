package com.vegw.compiler.IR.Tree;

import com.vegw.compiler.Utils.Constants;

public class MoveAddr extends Mem {
    protected Expr scale = new Imme(Constants.PointerSize);
    protected Expr scaledOffset;
    protected Expr offset = new Imme(Constants.IntSize);

    public MoveAddr(Expr base, Expr scaledOffset) {
        super(base);
        this.scaledOffset = scaledOffset;
    }

    public MoveAddr(Expr base, Expr scaledOffset, Expr offset) {
        super(base);
        this.scaledOffset = scaledOffset;
        this.offset = offset;
    }
}
