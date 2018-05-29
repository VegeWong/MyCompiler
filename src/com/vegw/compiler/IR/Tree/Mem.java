package com.vegw.compiler.IR.Tree;

public class Mem extends Expr {
    public Expr addr;
    public Mem(Expr a) { addr = a; }
}
