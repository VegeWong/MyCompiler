package com.vegw.compiler.NASM;

public class Register {
    public String name;
    private int index;
    private boolean isCalleeSave;

    public Register(String name, int index, boolean isCalleeSave) {
        this.name = name;
        this.index = index;
        this.isCalleeSave = isCalleeSave;
    }

    public int getIndex() { return index; }

    public boolean isCalleeSave() { return isCalleeSave; }
}
