package com.vegw.compiler.FrontEnd;

import com.vegw.compiler.AST.ASTNode;

public class ConstReintepreter extends Visitor {
    protected ASTNode ast;

    public ConstReintepreter(ASTNode ast) {
        this.ast = ast;

    }
}
