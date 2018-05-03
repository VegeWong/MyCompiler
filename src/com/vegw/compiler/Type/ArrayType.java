package com.vegw.compiler.Type;

public class ArrayType extends Type {
    protected Type baseType;
    protected int demension;

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
    @Override
    public boolean isSameType(Type a) {
        if (a instanceof ArrayType)
            return (((ArrayType) a).baseType == baseType
                    && ((ArrayType) a).demension == demension);
        else return false;
    }

    @Override
    public String toString() {
        return "array baseType:" + baseType.toString() + " demension: " + demension;
    }
}
