package com.vegw.compiler.Type;

import com.vegw.compiler.AST.Expr.Literal.NullLiteralNode;

public class StringType extends Type {

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
