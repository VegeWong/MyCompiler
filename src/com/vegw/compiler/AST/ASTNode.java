package com.vegw.compiler.AST;

import com.vegw.compiler.AST.Stmt.Def.DefinitionNode;
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
        this.constantTable = new ConstantTable();
    }

    public void setScope(Scope scope) {
        this.scope = scope;
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
