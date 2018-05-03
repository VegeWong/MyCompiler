package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.AST.Expr.ExprNode;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class ForNode extends StmtNode {

    protected ExprNode init;
    protected ExprNode cond;
    protected ExprNode step;
    protected StmtNode body;
    protected Location location;

    public ForNode(Location location, ExprNode init, ExprNode cond,
                   ExprNode step, StmtNode body) {
        this.location = location;
        this.init = init;
        this.cond = cond;
        this.step = step;
        this.body = body;
    }

    public ExprNode init() {
        return init;
    }

    public ExprNode cond() {
        return cond;
    }

    public ExprNode step() {
        return step;
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
