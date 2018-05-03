package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

public class MemberNode extends ExprNode {
    protected ExprNode field;
    protected String member;
    protected Type type;
    public MemberNode(ExprNode field, String member) {
        this.field = field;
        this.member = member;
        this.type = null;
    }

    public ExprNode field() {
        return field;
    }

    public String member() { return member; }

    public void setType(Type type) { this.type = type; }
    @Override
    public Location location() {
        return field.location();
    }

    @Override
    public String toString() {
        return "MemberNode:\n";
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Type type() {
        return type;
    }

}
