package com.vegw.compiler.Type;

import com.vegw.compiler.Utils.Constants;

public class IntegerType extends Type {

    public IntegerType() { super.size = Constants.IntSize; }

    @Override
    public boolean isConvertable(Type t) {
        return (t == Type.INT);
    }

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        return (a instanceof IntegerType);
    }

    @Override
    public String toString() { return "int"; }
}
