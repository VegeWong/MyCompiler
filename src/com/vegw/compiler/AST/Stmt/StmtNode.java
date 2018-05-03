package com.vegw.compiler.AST.Stmt;

import com.vegw.compiler.AST.Node;
import com.vegw.compiler.FrontEnd.ASTVisitor;

abstract public class StmtNode extends Node {
   abstract public <S, E> S accept(ASTVisitor<S, E> visitor);
}
