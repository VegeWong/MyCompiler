package com.vegw.compiler.IR.Tree;

public class Return extends Expr {
    public Expr value;
    public Return(Expr v) {
        value = v;
    }
}
