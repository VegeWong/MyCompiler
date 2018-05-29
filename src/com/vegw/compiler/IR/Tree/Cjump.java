package com.vegw.compiler.IR.Tree;

public class Cjump extends Expr {
    public Expr cond;
    public Label thenLabel;
    public Label elseLabel;

    public Cjump(Expr c, Label t, Label e) {
        cond = c;
        thenLabel = t;
        elseLabel = e;
    }
}
