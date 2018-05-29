package com.vegw.compiler.Type;

import com.vegw.compiler.Utils.Constants;

public class NullType extends Type {

    public NullType() { super.size = Constants.PointerSize; }

    @Override
    public boolean isConvertable(Type t) {
        return t.isConvertable(Type.NULL);
    }

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        return (a instanceof NullType);
    }

    @Override
    public String toString() { return "null"; }

}
