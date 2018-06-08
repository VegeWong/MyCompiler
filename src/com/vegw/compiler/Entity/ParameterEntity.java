package com.vegw.compiler.Entity;

import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

public class ParameterEntity extends Entity {
    protected Type type;
    protected Scope scope;

    public ParameterEntity(Location location, String name, Type type) {
        super(location, name);
        this.type = type;
        this.scope = null;
    }

    public void setScope(Scope scope) { this.scope = scope; }

    public Scope scope() { return this.scope; }

    public Type type() {
        return type;
    }
}
