package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.Entity.ClassEntity;
import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.Entity.VariableEntity;
import com.vegw.compiler.Exception.SemanticException;
import com.vegw.compiler.Utils.Constants;

import java.util.List;
import java.util.Map;

abstract public class Scope {
    protected List<LocalScope> children;
    protected Map<String, Entity> entities;

    abstract public Entity get(String name) throws SemanticException;

    public abstract Entity getCurrentScope(String name);

    public abstract Map<String, Entity> entities();

    public int setMemberOffset(ClassEntity field) {
        int offset = 0;
        for (String key : entities.keySet()) {
            Entity entity = entities.get(key);
            entity.setThisPtr(field);
            if (entity instanceof VariableEntity) {
                ((VariableEntity) entity).setOffset(offset);
                offset += Constants.WordSize;
            }
        }
        return offset;
    }
}
