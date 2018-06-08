package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class  BreakNode extends StmtNode {

    protected Location location;

    public BreakNode(Location location) { this.location = location; }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "Break:\n";
    }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}