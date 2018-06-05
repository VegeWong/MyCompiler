package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.BackEnd.Translator;

public class Jump extends Expr {
    public Label target;
    public Jump(Label t) { target = t; }
//
//    @Override
//    public Operand accept(InstructionSelector is) { return is.visit(this); }
    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }
}
