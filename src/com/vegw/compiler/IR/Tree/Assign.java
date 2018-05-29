package com.vegw.compiler.IR.Tree;

public class Assign extends Expr {
    public Expr left;
    public Expr right;

    public Assign(Expr l, Expr r) {
        left = l;
        right = r;
    }
}
