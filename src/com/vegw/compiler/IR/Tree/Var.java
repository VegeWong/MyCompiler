package com.vegw.compiler.IR.Tree;

import com.vegw.compiler.Entity.Entity;

public class Var extends Expr {
    public Entity entity;
    public Var(Entity e) { entity = e; }
}
