package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.Entity.StringConstEntity;
import com.vegw.compiler.NASM.Operand.Operand;

public class Str extends Expr {
    public StringConstEntity entity;
    public Str(StringConstEntity e) {entity = e;}

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }
}
