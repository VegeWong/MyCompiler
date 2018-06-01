package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.NASM.Operand.Operand;

import java.util.List;

public class Call extends Expr {
    public FunctionEntity func;
    public List<Var> args;

    public Call(FunctionEntity f, List<Var> a) {
        func = f;
        args = a;
    }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }
}
