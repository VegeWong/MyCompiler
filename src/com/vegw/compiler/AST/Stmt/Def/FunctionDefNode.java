package com.vegw.compiler.AST.Stmt.Def;

import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class FunctionDefNode extends DefinitionNode {
    FunctionEntity entity;
    public FunctionDefNode(FunctionEntity entity) {
        this.entity = entity;
    }

    public FunctionEntity entity() {
        return entity;
    }
    @Override
    public Location location() {
        return entity.location();
    }

    @Override
    public String toString() {
        return "FuncDef";
    }
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
