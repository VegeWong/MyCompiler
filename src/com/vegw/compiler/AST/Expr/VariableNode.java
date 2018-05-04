package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.Entity.ClassEntity;
import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.ClassType;
import com.vegw.compiler.Type.FunctionType;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;



public class VariableNode extends ExprNode {

    protected Location location;
    protected String name;
    protected Entity entity;
    protected Type type;
    protected boolean isAbleToSelfAddAndMinus;

    public VariableNode(Location location, String name) {
        this.location = location;
        this.name = name;
        this.entity = null;
        super.isAssignable = true;
        isAbleToSelfAddAndMinus = false;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity instanceof FunctionEntity) {
            type = new FunctionType(entity.name(), (FunctionEntity) entity);
            super.isAssignable = false;
        }
        else if (entity instanceof ClassEntity) type = new ClassType(entity.name(), (ClassEntity) entity);
        else {
            type = ((VariableEntity) entity).type();
            if (type == Type.INT && ((VariableEntity) entity).value() != null)
                isAbleToSelfAddAndMinus = true;
        }

    }

    public boolean isAbleToSelfAddAndMinus() {
        return isAbleToSelfAddAndMinus;
    }

    public Entity entity() {
        return entity;
    }

    public String name() { return name; }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "VariableNode";
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type type() {
        return type;
    }
}
