package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.Expr.VariableNode;
import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Exception.SemanticException;
import com.vegw.compiler.FrontEnd.Scope;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TopLevelScope extends Scope {


    public TopLevelScope() {
        super.entities = new LinkedHashMap<String, Entity>();
    }

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

