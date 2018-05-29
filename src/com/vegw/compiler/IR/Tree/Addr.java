package com.vegw.compiler.IR.Tree;

import com.vegw.compiler.Entity.Entity;

public class Addr extends Expr {
    Entity entity;
    public Addr(Entity e) { entity = e; }
}
