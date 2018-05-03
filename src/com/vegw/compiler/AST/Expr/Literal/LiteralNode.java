package com.vegw.compiler.AST.Expr.Literal;

import com.vegw.compiler.AST.Expr.ExprNode;

abstract public class LiteralNode extends ExprNode {
    public LiteralNode() { super.isAssignable = false; }
}
