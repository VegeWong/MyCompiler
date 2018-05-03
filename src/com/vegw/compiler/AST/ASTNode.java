package com.vegw.compiler.AST;

import com.vegw.compiler.AST.Stmt.Def.ClassDefNode;
import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
import com.vegw.compiler.AST.Stmt.Def.FunctionDefNode;
import com.vegw.compiler.AST.Stmt.Def.VariableDefNode;
import com.vegw.compiler.FrontEnd.ConstantTable;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.Utils.Location;

import java.util.List;

public class ASTNode extends Node {
    public Location location;
    public List<DefinitionNode> defs;
    public Scope scope;
    public ConstantTable constantTable;

    public ASTNode(Location location, List<DefinitionNode> defs) {
        this.location = location;
        this.defs = defs;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public void setConstantTable(ConstantTable constantTable) {
        this.constantTable = constantTable;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public String toString() {
        return "ASTNode";
    }
}
