package com.vegw.compiler.Type;

import com.vegw.compiler.Utils.Constants;

public class BoolType extends Type {

    public BoolType() { super.size = Constants.BoolSize; }

    @Override
    public boolean isConvertable(Type t) {
        return (t == Type.BOOL);
    }

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
