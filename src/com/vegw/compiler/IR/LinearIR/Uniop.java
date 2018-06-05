package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.IR.LinearIR.Operand.Operand;

public class Uniop extends Expr {
    public enum UniOp {
        LNOT,
        BNOT,
        NEG
    }

    public UniOp operator;
    public Operand operand;
    public Uniop(UniOp op, Operand e) {
        operator = op;
        operand = e;
    }

//    @Override
//    public Operand accept(InstructionSelector is) { return is.visit(this); }

    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }
}
