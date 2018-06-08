package com.vegw.compiler.AST.Expr.Literal;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Constants;
import com.vegw.compiler.Utils.Location;

public class IntegerLiteralNode extends LiteralNode {
    protected int value;
    protected Location location;

    public IntegerLiteralNode(Location location, int value) {
        super();
        this.value = value;
        this.location = location;
    }

    public int value() { return this.value; }

    @Override
    public String toString() { return "IntegerLiteral"; }

    @Override
    public Location location() { return location; }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type type() {
        return Type.INT;
    }

    @Override
    public int size() { return Constants.IntSize; }
}
