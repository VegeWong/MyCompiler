package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.Expr.VariableNode;
import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.Exception.SemanticException;

import java.util.LinkedHashMap;
import java.util.Map;

public class LocalScope extends Scope {
    protected Scope parent;
    protected Map<String, Entity> entities;

    public LocalScope(Scope parent) {
        this.parent = parent;
        this.entities = new LinkedHashMap<>();
    }

    public Map<String, Entity> entities() {
        return entities;
    }

    public Entity get(String name) throws SemanticException {
        Entity var = entities.get(name);
        if (var != null) return var;
        else return parent.get(name);
    }

    public Entity getCurrentScope(String name) {
        return entities.get(name);
    }
}
