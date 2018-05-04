package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

import java.util.List;

public class CreatorNode extends ExprNode {

    protected Location location;
    protected Type type;
    protected List<ExprNode> dimensionExprs;

    public CreatorNode(Location location, Type type, List<ExprNode> dimensionExprs) {
        super.isAssignable = false;
        this.location = location;
        this.type = type;
        this.dimensionExprs = dimensionExprs;
    }

    public Type type() { return type; }

    public List<ExprNode> dimensionExpr() {
        return dimensionExprs;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "CreatorNode";
    }

    public <S, E> E accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
