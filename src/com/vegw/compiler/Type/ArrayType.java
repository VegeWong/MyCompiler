package com.vegw.compiler.Type;

import com.vegw.compiler.Entity.ArrayEntity;

public class ArrayType extends Type {
    protected Type baseType;
    protected int demension;
    protected final static ArrayEntity entity = new ArrayEntity();

    public ArrayType(Type baseType, int demension) {
        this.baseType = baseType;
        this.demension = demension;
    }

    public int demension() {
        return demension;
    }

    public Type baseType() {
        return baseType;
    }

    public ArrayEntity entity() { return entity; }

    @Override
    public boolean isConvertable(Type t) {
        return (t == Type.NULL || this.isSameType(t));
    }

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        if (a instanceof ArrayType)
            return (((ArrayType) a).baseType.isSameType(baseType)
                    && ((ArrayType) a).demension == demension);
        else return false;
    }

    @Override
    public String toString() {
        return "array baseType:" + baseType.toString() + " demension: " + demension;
    }
}
