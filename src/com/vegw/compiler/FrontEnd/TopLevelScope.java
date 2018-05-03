package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.Expr.VariableNode;
import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Exception.SemanticException;
import com.vegw.compiler.FrontEnd.Scope;

import java.util.List;
import java.util.Map;

public class TopLevelScope extends Scope {
    protected Map<String, Entity> entities;

    @Override
    public Entity get(String name) throws SemanticException {
        Entity var = entities.get(name);
        if (var == null)
            throw new SemanticException("unresolved reference: " + name);
        else
            return var;
    }

    @Override
    public Entity getCurrentScope(String name) {
        return entities.get(name);
    }

    @Override
    public Map<String, Entity> entities() {
        return entities;
    }
}

