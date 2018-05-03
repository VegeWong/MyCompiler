package com.vegw.compiler.Entity;

import com.vegw.compiler.AST.Expr.ExprNode;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

import java.util.LinkedList;

public class VariableEntity extends Entity {
    protected Type type;
    protected ExprNode value;
    protected Scope scope;

    public VariableEntity(Location location, String name,
                          Type type) {
        super(location, name);
        this.type = type;
        this.value = null;
        this.scope = null;
    }

    public void setScope(Scope scope) { this.scope = scope; }

    public String name() { return super.name; }

    public Type type() { return type; }

    public Scope scope() { return this.scope; }

    public ExprNode value() { return value; }

    public void setValue(ExprNode value) {
        this.value = value;
    }
}
