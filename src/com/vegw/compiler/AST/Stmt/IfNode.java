package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.AST.Expr.ExprNode;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class IfNode extends StmtNode {

    protected ExprNode cond;
    protected StmtNode thenBody;
    protected StmtNode elseBody;
    protected Location location;

    public IfNode(Location location, ExprNode cond,
                  StmtNode thenBody, StmtNode elseBody) {
        this.location = location;
        this.cond = cond;
        this.thenBody = BlockNode.cast(thenBody);
        this.elseBody = BlockNode.cast(elseBody);
    }

    public ExprNode cond() {
        return cond;
    }

    public StmtNode thenBody() {
        return thenBody;
    }

    public StmtNode elseBody() {
        return elseBody;
    }

    @Override
    public Location location() {
        return location;
    }
    @Override
    public String toString() {
        String str = "If:";
        return str;
    }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}