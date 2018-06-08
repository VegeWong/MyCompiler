package com.vegw.compiler.AST.Expr.Literal;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Constants;
import com.vegw.compiler.Utils.Location;

public class StringLiteralNode extends LiteralNode {

    protected String value;
    protected Location location;

    public StringLiteralNode(Location location, String value) {
        super();
        this.value = value;
        this.location = location;
    }

    public String value() { return this.value; }

    public String toString() { return "StringLiteral"; }

    public Location location() { return location; }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type type() {
        return Type.STRING;
    }

    @Override
    public int size() { return Constants.AddrSize; }
}
