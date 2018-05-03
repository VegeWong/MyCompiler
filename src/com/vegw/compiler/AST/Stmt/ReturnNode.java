package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.AST.Expr.ExprNode;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class ReturnNode extends StmtNode {

    protected ExprNode expr;
    protected Location location;

    public ReturnNode(Location location, ExprNode expr) {
        this.expr = expr;
        this.location = location;
    }

    public ExprNode expr() { return this.expr; }

    @Override
    public Location location() { return this.location; }

    @Override
    public String toString() { return "Return:\n"; }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
