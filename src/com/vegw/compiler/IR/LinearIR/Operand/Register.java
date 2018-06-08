package com.vegw.compiler.IR.LinearIR.Operand;

abstract public class Register extends Operand {
    abstract public int id();
    public boolean inReg;
}
