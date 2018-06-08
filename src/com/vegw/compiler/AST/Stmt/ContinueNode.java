package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class  ContinueNode extends StmtNode {

    protected Location location;

    public ContinueNode(Location location) { this.location = location; }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "Continue:\n";
    }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
