package com.vegw.compiler.IR.Tree;

import com.vegw.compiler.Entity.FunctionEntity;

import java.util.List;

public class Call extends Expr {
    public FunctionEntity func;
    public List<Expr> args;

    public Call(FunctionEntity f, List<Expr> a) {
        func = f;
        args = a;
    }
}
