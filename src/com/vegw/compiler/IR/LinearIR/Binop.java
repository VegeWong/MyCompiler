package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.IR.LinearIR.Operand.Operand;

public class Binop extends Expr {
    public enum BinOp {
        ADD, SUB, MUL, DIV, MOD,
        LSH, RSH,
        LT, GT, LE, GE, EQ, NE,
        AND, OR, XOR
    }

    public BinOp operator;
    public Operand left;
    public Operand right;

    public Binop(BinOp op, Operand l , Operand r) {
        operator = op;
        left = l;
        right = r;
    }

//    @Override
//    public Operand accept(InstructionSelector is) { return is.visit(this); }

    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }
}
