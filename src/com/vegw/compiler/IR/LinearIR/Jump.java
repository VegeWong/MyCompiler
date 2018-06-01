package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Operand.Operand;

public class Jump extends Expr {
    public Label target;
    public Jump(Label t) { target = t; }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }}
