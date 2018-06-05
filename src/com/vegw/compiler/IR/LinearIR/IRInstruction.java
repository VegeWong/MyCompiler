package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;
import com.vegw.compiler.BackEnd.Translator;

abstract public class IRInstruction {
    private IRInstruction prev;
    private IRInstruction next;

    public void setPrev(IRInstruction prev) { this.prev = prev; }

    public void setNext(){}
//    abstract public Operand accept(InstructionSelector is);
    abstract public void accept(Translator translator);
}
