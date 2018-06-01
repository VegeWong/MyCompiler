package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.NASM.Operand.Operand;

abstract public class IRInstruction {
    private IRInstruction prev;
    private IRInstruction next;

    public void setPrev(IRInstruction prev) { this.prev = prev; }

    public void setNext(){}
    abstract public Operand accept(InstructionSelector is);
}
