package com.vegw.compiler.Entity;

import com.vegw.compiler.AST.Stmt.BlockNode;
import com.vegw.compiler.FrontEnd.Scope;
import com.vegw.compiler.IR.LinearIR.IRInstruction;
import com.vegw.compiler.IR.LinearIR.Label;
import com.vegw.compiler.NASM.Instruction.Instruction;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;
import com.vegw.compiler.Type.Type;
import com.vegw.compiler.Utils.Location;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FunctionEntity extends Entity {
    protected Type returnType;
    protected List<ParameterEntity> params;
    protected BlockNode body;
    protected boolean isConstructor;
    protected Scope scope;

    // For IR
    public Label begin;
    public Label end;
    public boolean callOtherFunc = false;
    public int size = -1;

    public List<IRInstruction> irInstructions = new LinkedList<IRInstruction>();
    public List<Instruction> inst = new LinkedList<Instruction>();

    public int virtualRegisterCnt;
    private Map<Entity, VirtualRegister> map = new HashMap<Entity, VirtualRegister>();

    public void pushAllParams() {
        if (params.size() < 6) {
            virtualRegisterCnt = 1;
            for (int i = 0; i < params.size(); ++i) {
                Entity entity = scope.entities().get(params.get(i).name);
                map.put(entity, new VirtualRegister(virtualRegisterCnt++));
            }
        } else {
            virtualRegisterCnt = -2;
            for (int i = 6; i < params.size(); ++i) {
                Entity entity = scope.entities().get(params.get(i).name);
                map.put(entity, new VirtualRegister(virtualRegisterCnt--));
            }
            virtualRegisterCnt = 1;
            for (int i = 0; i < 6; ++i) {
                Entity entity = scope.entities().get(params.get(i).name);
                map.put(entity, new VirtualRegister(virtualRegisterCnt++));
            }
        }
    }

    public VirtualRegister getVReg(Entity entity) {
        VirtualRegister reg = map.get(entity);
        if (reg != null) return reg;
        reg = new VirtualRegister(virtualRegisterCnt++);
        map.put(entity, reg);
        return reg;
    }

    public int size() {
        if (size == -1) {
            size = params.size() > 6? 6: params.size();
            size += scope.entities().size();
        }
        return size;
    }

    public void addInst(Instruction ins) {
        inst.add(ins);
    }

    public void addIRInst(IRInstruction ins) {
        irInstructions.add(ins);
    }

    public IRInstruction getLastIRInst() {
        return irInstructions.get(irInstructions.size() - 1);
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

    public Scope scope() { return scope; }

    public void setScope(Scope scope) { this.scope = scope; }

    public void setConstructor(){
        isConstructor = true;
    }

    public boolean isConstructor() {
        return isConstructor;
    }
}
