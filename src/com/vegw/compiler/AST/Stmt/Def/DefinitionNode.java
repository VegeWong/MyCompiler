package com.vegw.compiler.AST.Stmt.Def;

import com.vegw.compiler.AST.Stmt.StmtNode;
import com.vegw.compiler.Entity.Entity;

abstract public class DefinitionNode extends StmtNode {
    abstract public Entity entity();
}
