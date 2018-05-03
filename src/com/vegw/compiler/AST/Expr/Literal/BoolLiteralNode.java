package com.vegw.compiler.AST.Expr.Literal;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

public class BoolLiteralNode extends LiteralNode {

    protected boolean value;
    protected Location location;

    public BoolLiteralNode(Location loc, boolean val) {
        value = val;
        location = loc;
    }

    public boolean value() {
        return value;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "BoolLiteral";
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type type(){
        return Type.BOOL;
    }
}
