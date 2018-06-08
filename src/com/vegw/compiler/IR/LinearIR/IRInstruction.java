package com.vegw.compiler.IR.LinearIR;

//import com.vegw.compiler.BackEnd.InstructionSelector;

import com.vegw.compiler.BackEnd.IRInstructionVisitor;
import com.vegw.compiler.BackEnd.Translator;
import com.vegw.compiler.IR.LinearIR.Operand.Register;

import java.util.HashSet;
import java.util.Set;

abstract public class IRInstruction {
    // For liveness analyze and register coloring
    public Set<IRInstruction> pred = new HashSet<IRInstruction>();
    public Set<IRInstruction> succ = new HashSet<IRInstruction>();

    public Set<Register> use = new HashSet<Register>();
    public Register def;
    public Set<Register> in = new HashSet<Register>();
    public Set<Register> out = new HashSet<Register>();

    public void addPred(IRInstruction p) { pred.add(p); }

    public void addSucc(IRInstruction s) { succ.add(s); }

    public void addUse(Register reg) { use.add(reg); }

    public void addDef(Register reg) { def =reg; }

//    abstract public Operand accept(InstructionSelector is);
    abstract public void accept(Translator translator);

    abstract public void accept(IRInstructionVisitor irVisitor);
}
