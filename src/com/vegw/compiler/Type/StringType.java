package com.vegw.compiler.Type;

import com.vegw.compiler.Entity.StringEntity;
import com.vegw.compiler.Utils.Constants;

public class StringType extends Type {
    public final static StringEntity entity = new StringEntity();

    public StringEntity entity() {
        return entity;
    }

    public StringType() { super.size = Constants.PointerSize; }

    @Override
    public boolean isConvertable(Type other) {
        return (other == Type.STRING);
    }

    @Override
    public boolean isSameType(Type a) {
        if (this == a) return true;
        return (a instanceof StringType);
    }

    @Override
    public String toString() { return "string"; }

}
