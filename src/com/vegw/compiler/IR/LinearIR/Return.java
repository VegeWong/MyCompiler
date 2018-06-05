package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.IR.LinearIR.Operand.Operand;

public class Return extends Expr {
    public Operand value;
    public Return(Operand v) {
        value = v;
    }


//    @Override
//    public Operand accept(InstructionSelector is) { return is.visit(this); }

    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }
}
