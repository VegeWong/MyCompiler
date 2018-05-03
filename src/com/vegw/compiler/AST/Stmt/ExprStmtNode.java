package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.AST.Expr.ExprNode;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class ExprStmtNode extends StmtNode {
    protected ExprNode expr;

    public ExprStmtNode(ExprNode expr) {
        this.expr = expr;
    }

    public ExprNode expr() {
        return expr;
    }

    @Override
    public Location location() { return expr.location(); }

    @Override
    public String toString() {return "ExprStmt:\n"; }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
