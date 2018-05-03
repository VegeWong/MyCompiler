package com.vegw.compiler.AST.Stmt.Def;

import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class VariableDefNode extends DefinitionNode {
    VariableEntity entity;
    public VariableDefNode(VariableEntity entity) {
        this.entity = entity;
    }

    public VariableEntity entity() {
        return entity;
    }

    @Override
    public Location location() {
        return entity.location();
    }

    @Override
    public String toString() {
        return "VariableDef";
    }

    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }
}
