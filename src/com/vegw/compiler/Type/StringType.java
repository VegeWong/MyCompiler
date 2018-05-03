package com.vegw.compiler.Type;

import com.vegw.compiler.AST.Expr.Literal.NullLiteralNode;
import com.vegw.compiler.Entity.StringEntity;

public class StringType extends Type {
    private final static StringEntity entity = new StringEntity();

    public StringEntity entity() {
        return entity;
    }

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
