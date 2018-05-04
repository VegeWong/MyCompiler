package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

public class ArefNode extends ExprNode {

    protected ExprNode base;
    protected ExprNode index;
    protected Type type;


    public ArefNode(ExprNode base, ExprNode index) {
        super.isAssignable = true;
        this.base = base;
        this.index = index;
    }

    public boolean isMultiDimension() {
        return (base instanceof ArefNode);
    }

    public ExprNode baseExpr() {
        return isMultiDimension() ? ((ArefNode)base).baseExpr() : base;
    }

    public ExprNode base() { return base; }

    public ExprNode index() { return index; }

    public Type type() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Location location() {
        return base.location();
    }

    @Override
    public String toString() {
        String str;
        str = "ArefNode";
        return str;
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
