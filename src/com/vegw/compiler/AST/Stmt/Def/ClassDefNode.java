package com.vegw.compiler.AST.Stmt.Def;

import com.vegw.compiler.Entity.ClassEntity;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.Utils.Location;

public class ClassDefNode extends DefinitionNode {
    protected ClassEntity entity;
    public ClassDefNode(ClassEntity entity) {
        this.entity = entity;
    }

    public ClassEntity entity() {
        return entity;
    }
    @Override
    public Location location() {
        return entity.location();
    }

    @Override
    public String toString() {
        return "ClassDef";
    }
    public <S, E> S accept(ASTVisitor<S, E> visitor) {
        return visitor.visit(this);
    }

}
