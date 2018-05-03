package com.vegw.compiler.Type;

public class BoolType extends Type {

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        return (a instanceof BoolType);
    }

    @Override
    public String toString() {
        return "bool";
    }
}
