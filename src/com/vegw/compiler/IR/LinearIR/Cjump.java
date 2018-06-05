package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.BackEnd.Translator;

public class Cjump extends Expr {
    public Expr cond;
    public Label thenLabel;
    public Label elseLabel;

    public Cjump(Expr c, Label t, Label e) {
        cond = c;
        thenLabel = t;
        elseLabel = e;
    }

//    @Override
//    public Operand accept(InstructionSelector is) { return is.visit(this); }
    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }
}
