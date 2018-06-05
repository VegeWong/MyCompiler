package com.vegw.compiler.IR.LinearIR.Operand;

public class PhysicalRegister extends Register {
    public final static PhysicalRegister rax = new PhysicalRegister("rax", 0, false);
    public final static PhysicalRegister rbx = new PhysicalRegister("rbx", 1, true);
    public final static PhysicalRegister rcx = new PhysicalRegister("rcx", 2, false);
    public final static PhysicalRegister rdx = new PhysicalRegister("rdx", 3, false);
    public final static PhysicalRegister rsi = new PhysicalRegister("rsi", 4, false);
    public final static PhysicalRegister rdi = new PhysicalRegister("rdi", 5, false);
    public final static PhysicalRegister rbp = new PhysicalRegister("rbp", 6, true);
    public final static PhysicalRegister rsp = new PhysicalRegister("rsp", 7, false);
    public final static PhysicalRegister r8 = new PhysicalRegister("r8", 8, false);
    public final static PhysicalRegister r9 = new PhysicalRegister("r9", 9, false);
    public final static PhysicalRegister r10 = new PhysicalRegister("r10", 10, false);
    public final static PhysicalRegister r11 = new PhysicalRegister("r11", 11, false);
    public final static PhysicalRegister r12 = new PhysicalRegister("r12", 12, true);
    public final static PhysicalRegister r13 = new PhysicalRegister("r13", 13, true);
    public final static PhysicalRegister r14 = new PhysicalRegister("r14", 14, true);
    public final static PhysicalRegister r15 = new PhysicalRegister("r15", 15, true);

    public String name;
    public int index;
    public boolean isCalleeSave;

    public PhysicalRegister(String n, int i, boolean c) {
        name = n;
        index = i;
        isCalleeSave = c;
    }

    @Override
    public String toNASM() {
        return name;
    }
}