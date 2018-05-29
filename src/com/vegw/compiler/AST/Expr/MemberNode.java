package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.FunctionType;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Constants;
import com.vegw.compiler.Utils.Location;

public class MemberNode extends ExprNode {
    protected ExprNode field;
    protected String member;
    protected Type type;
    // Add entity, used in IR, maybe redundant with member:type
    protected Entity entity;

    public MemberNode(ExprNode field, String member) {
        super.isAssignable = false;
        this.field = field;
        this.member = member;
        this.type = null;
    }

    public void setIsAssignable(boolean is) {
        super.isAssignable = is;
    }

    public ExprNode field() {
        return field;
    }

    public String member() { return member; }

    public void setEntity(Entity entity) {
        this.entity = entity;
        if (entity instanceof FunctionEntity) type = new FunctionType(entity.name(), (FunctionEntity) entity);
        else { type = ((VariableEntity) entity).type(); setIsAssignable(true);}
    }

    @Override
    public Location location() {
        return field.location();
    }

    @Override
    public String toString() {
        return "MemberNode:\n";
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    public Entity entity() { return entity; }

    @Override
    public Type type() { return type; }

    @Override
    public int size() { return Constants.NullSize; }

}
