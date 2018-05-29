package com.vegw.compiler.IR.Tree;

import com.vegw.compiler.Entity.StringConstEntity;

public class Str extends Expr {
    public StringConstEntity entity;
    public Str(StringConstEntity e) {entity = e;}
}
