package com.vegw.compiler.Type;

public class IntegerType extends Type {

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        return (a instanceof IntegerType);
    }

    @Override
    public String toString() { return "int"; }
}
