package com.vegw.compiler.IR.Tree;

public class Label extends Expr {
    protected String name;
    public Label() { name = "__Label__"; }

    public Label(String name) { this.name = name; }

    public String name() { return name; }
}
