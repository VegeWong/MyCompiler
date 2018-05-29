package com.vegw.compiler.IR.Tree;

public class Jump extends Expr {
    Label target;
    public Jump(Label t) { target = t; }
}
