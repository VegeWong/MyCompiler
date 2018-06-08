package com.vegw.compiler.Entity;

import com.vegw.compiler.IR.LinearIR.Operand.GlobalVarible;
import com.vegw.compiler.Utils.Location;

abstract public class Entity {
    protected String name;
    protected Location location;
    protected String internalName = "__DefaultInternalName__";

    protected ClassEntity thisPtr = null;
    protected boolean isMember = false;
    protected boolean isGlobalVariables = false;
    protected int offset = 0;


    public GlobalVarible gvalue = null;

    public Entity(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public void setThisPtr(ClassEntity entity) { thisPtr = entity; }

    public ClassEntity thisPtr() { return thisPtr; }

    public void setMember(boolean isMember) { this.isMember = isMember; }

    public boolean isMember() { return isMember; }

    public void setGlobalVariables(GlobalVarible gv) {
        gvalue = gv;
        isGlobalVariables = true;
    }

    public boolean isGlobalVariables() { return isGlobalVariables; }

    public int offset() { return offset; }

    public void rename(String internalName) { this.internalName = internalName;}

    public Location location() { return location; }

    public String internalName() { return internalName; }

    public String name(){
        return name;
    }



}
