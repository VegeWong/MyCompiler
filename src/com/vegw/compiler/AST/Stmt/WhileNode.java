package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.AST.Expr.ExprNode;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class WhileNode extends StmtNode {
    protected ExprNode cond;
    protected StmtNode body;
    protected Location location;

    public WhileNode(Location location, ExprNode cond, StmtNode body) {
        this.location = location;
        this.cond = cond;
        this.body = body;
    }

    public ExprNode cond() {
        return cond;
    }

    public StmtNode body() {
        return body;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "While:\n";
    }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
