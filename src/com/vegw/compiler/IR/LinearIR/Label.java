package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;

import com.vegw.compiler.BackEnd.IRInstructionVisitor;
import com.vegw.compiler.BackEnd.Translator;

public class Label extends Expr {
    protected String name;

    public Label() {
        name = "__Label__";
    }

    public Label(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

//    @Override
//    public Operand accept(InstructionSelector is) {
//        return is.visit(this);
//    }

    @Override
    public void accept(Translator translator) {
        translator.visit(this);
    }

    @Override
    public void accept(IRInstructionVisitor irVisitor) {
        irVisitor.visit(this);
    }

    public String toNASM() {
        return name;
    }
}
