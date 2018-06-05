package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.Entity.FunctionEntity;

public class Call extends Expr {
    public FunctionEntity func;

    public Call(FunctionEntity f) {
        func = f;
    }

//    @Override
//    public Operand accept(InstructionSelector is) { return is.visit(this); }

    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }

}

