package com.vegw.compiler.Entity;

import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.Type.ClassType;
import com.vegw.compiler.Utils.Location;

import java.util.List;

public class ClassEntity extends Entity {

    protected FunctionDefNode constructor;
    protected List<FunctionDefNode> funcs;
    protected List<VariableDefNode> vars;
    protected Scope scope;
    protected ClassType classType;
    protected int size;

    public ClassEntity(Location location, String name,
                       List<FunctionDefNode> funcs,
                       List<VariableDefNode> vars) {
        super(location, name);
        super.thisPtr = this;
        this.funcs = funcs;
        this.vars = vars;
        classType = null;
        scope = null;
    }

    public int size() { return size; }

    public ClassType classType() { return classType; }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope scope() {
        return scope;
    }

    public void setOffset() { this.size = scope.setMemberOffset(this); }

    public FunctionDefNode constructor() {
        return constructor;
    }

    public List<FunctionDefNode> funcs() {
        return funcs;
    }

    public List<VariableDefNode> vars() {
        return  vars;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public void setConstructor(FunctionDefNode constructor){
        this.constructor = constructor;
    }
}
