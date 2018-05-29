package com.vegw.compiler.IR.Tree;

public class Uniop extends Expr {
    public enum UniOp {
        NOT,
        NEG
    }

    public UniOp operator;
    public Expr expr;
    public Uniop(UniOp op, Expr e) {
        operator = op;
        expr = e;
    }
}
