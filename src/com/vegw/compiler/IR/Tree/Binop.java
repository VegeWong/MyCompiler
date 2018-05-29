package com.vegw.compiler.IR.Tree;

public class Binop extends Expr {
    public enum BinOp {
        ADD, SUB, MUL, DIV, MOD,
        LSH, RSH,
        LT, GT, LE, GE, EQ, NE,
        AND, OR, XOR
    }

    public BinOp operator;
    public Expr left;
    public Expr right;

    public Binop(BinOp op, Expr l , Expr r) {
        operator = op;
        left = l;
        right = r;
    }

}
