package com.vegw.compiler.Type;

public class VoidType extends Type {

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        return (a instanceof VoidType);
    }

    @Override
    public String toString() { return "void"; }

}
