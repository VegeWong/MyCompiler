package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.Entity.Entity;
import com.vegw.compiler.NASM.Operand.Operand;

public class Var extends Expr {
    public Entity entity;
    public Var(Entity e) { entity = e; }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }
}
