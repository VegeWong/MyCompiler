package com.vegw.compiler.Type;

import com.vegw.compiler.Entity.ClassEntity;
import com.vegw.compiler.Utils.Constants;

public class ClassType extends Type {
    protected String name;
    protected ClassEntity entity;


    public ClassType(String name) {
        this.name = name;
        this.entity = null;
        super.size = Constants.PointerSize;
    }

    public ClassType(String name, ClassEntity entity) {
        this.name = name;
        this.entity = entity;
    }

    public String name() {
        return name;
    }

    public void setEntity(ClassEntity entity) {
        this.entity = entity;
        entity.setClassType(this);
    }

    public ClassEntity entity() {
        return entity;
    }

    @Override
    public boolean isConvertable(Type t) {
        return (t == Type.NULL || this.isSameType(t));
    }

    @Override
    public boolean isSameType(Type a) {
        if (a instanceof ClassType)
            return this.entity.name() == ((ClassType) a).entity.name();
        return false;
    }

    @Override
    public String toString() {
        return "Class: "  + this.entity.name() + " Name: " + name;
    }
}
