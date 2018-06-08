package com.vegw.compiler.AST.Expr;

import com.vegw.compiler.AST.Node;
import com.vegw.compiler.FrontEnd.ASTVisitor;
import com.vegw.compiler.IR.LinearIR.Label;
import com.vegw.compiler.Type.Type;

abstract public class ExprNode extends Node {
    protected boolean isAssignable;
    protected boolean isDetermined;
    public boolean isAssignable() { return isAssignable; }
    abstract public <S, E> E accept(ASTVisitor<S, E> visitor);
    abstract public Type type();
    abstract public int size();
    // IR
    public Label ifTrue = null;
    public Label ifFalse = null;
}
