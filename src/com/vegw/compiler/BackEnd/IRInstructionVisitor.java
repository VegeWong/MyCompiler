package com.vegw.compiler.BackEnd;

import com.vegw.compiler.IR.LinearIR.*;

abstract public class IRInstructionVisitor {

    protected void uvisit(IRInstruction ins) {
        ins.accept(this);
    }

    abstract public void visit(Assign ins);

    abstract public void visit(Binop ins);

    abstract public void visit(Call ins);

    abstract public void visit(Cjump ins);

    abstract public void visit(Jump ins);

    abstract public void visit(Label ins);

    abstract public void visit(Push ins);

    abstract public void visit(Return ins);

    abstract public void visit(Uniop ins);
}
