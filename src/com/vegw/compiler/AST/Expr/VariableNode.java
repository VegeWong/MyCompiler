package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;



public class VariableNode extends ExprNode {

    protected Location location;
    protected String name;
    protected VariableEntity entity;

    public VariableNode(Location location, String name) {
        this.location = location;
        this.name = name;
        this.entity = null;
        super.isAssignable = true;
    }

    public void setEntity(VariableEntity entity) {
        this.entity = entity;
    }

    public VariableEntity entity() {
        return entity;
    }

    public String name() { return name; }
    @Override
    public Location location() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type type() {
        return entity.type();
    }
}
