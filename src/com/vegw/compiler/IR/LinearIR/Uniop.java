package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Operand.Operand;

public class Uniop extends Expr {
    public enum UniOp {
        LNOT,
        BNOT,
        NEG
    }

    public UniOp operator;
    public Expr expr;
    public Uniop(UniOp op, Expr e) {
        operator = op;
        expr = e;
    }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }
}
