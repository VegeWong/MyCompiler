package com.vegw.compiler.Type;

public class VoidType extends Type {

    public VoidType() { super.size = 0; }

    @Override
    public boolean isConvertable(Type t) {
        return (t == Type.VOID);
    }

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        return (a instanceof VoidType);
    }

    @Override
    public String toString() { return "void"; }

}
