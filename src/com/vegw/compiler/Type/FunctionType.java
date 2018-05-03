package com.vegw.compiler.Type;

import com.vegw.compiler.Entity.FunctionEntity;

public class FunctionType extends Type {
    protected String name;
    protected FunctionEntity entity;

    public FunctionType(String name) {
        this.name = name;
        this.entity = null;
    }

    public FunctionType(String name, FunctionEntity entity) {
        this.name = name;
        this.entity = entity;
    }

    public String name() {
        return name;
    }

    public FunctionEntity entity() {
        return entity;
    }

    public void setEntity(FunctionEntity entity) {
        this.entity = entity;
    }

    public FunctionEntity getEntity() {
        return entity;
    }

    @Override
    public boolean isConvertable(Type t) {
        return isSameType(t);
    }

    @Override
    public boolean isSameType(Type a) {
        if (a instanceof FunctionType)
            return this.name == ((FunctionType) a).name;
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
