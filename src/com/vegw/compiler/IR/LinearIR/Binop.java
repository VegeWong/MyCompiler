package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Operand.Operand;

public class Binop extends Expr {
    public enum BinOp {
        ADD, SUB, MUL, DIV, MOD,
        LSH, RSH,
        LT, GT, LE, GE, EQ, NE,
        AND, OR, XOR
    }

    public BinOp operator;
    public Expr left;
    public Expr right;

    public Binop(BinOp op, Expr l , Expr r) {
        operator = op;
        left = l;
        right = r;
    }

    @Override
    public Operand accept(InstructionSelector is) { return is.visit(this); }

}
