package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Exception.SemanticException;

import java.util.List;
import java.util.Map;

abstract public class Scope {
    protected List<LocalScope> children;
    abstract public Entity get(String name) throws SemanticException;

    public abstract Entity getCurrentScope(String name);

    public abstract Map<String, Entity> entities();
}
