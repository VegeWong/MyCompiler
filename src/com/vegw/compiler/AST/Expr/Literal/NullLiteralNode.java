package com.vegw.compiler.AST.Expr.Literal;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Constants;
import com.vegw.compiler.Utils.Location;

public class NullLiteralNode extends LiteralNode{
    Location location;

    public NullLiteralNode(Location location) {
        this.location = location;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "NullLiteral";
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type type() {
        return Type.NULL;
    }

    @Override
    public int size() { return Constants.NullSize; }
}
