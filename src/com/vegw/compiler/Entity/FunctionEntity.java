package com.vegw.compiler.Entity;

import com.vegw.compiler.AST.Stmt.BlockNode;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.IR.LinearIR.IRInstruction;
import com.vegw.compiler.IR.LinearIR.Label;
import com.vegw.compiler.NASM.Instruction.Instruction;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

import java.util.LinkedList;
import java.util.List;

public class FunctionEntity extends Entity {
    protected Type returnType;
    protected List<ParameterEntity> params;
    protected BlockNode body;
    protected boolean isConstructor;
    protected Scope scope;

    // For IR
    public Label begin;
    public Label end;

    private List<IRInstruction> irInstructions = new LinkedList<IRInstruction>();
    private List<Instruction> inst = new LinkedList<Instruction>();

    public void addIRInst(IRInstruction ins) {
        irInstructions.add(ins);
    }


    public FunctionEntity(Location location, String name,
                          Type returnType, List<ParameterEntity> params,
                          BlockNode body) {
        super(location, name);
        this.returnType = returnType;
        this.params = params;
        this.body = body;
        this.isConstructor = false;
        this.scope = null;
    }

    public Type returnType() { return returnType; }

    public List<ParameterEntity> params() {
        return params;
    }

    public BlockNode body() { return body; }

    public void setScope(Scope scope) { this.scope = scope; }

    public void setConstructor(){
        isConstructor = true;
    }

    public boolean isConstructor() {
        return isConstructor;
    }
}
