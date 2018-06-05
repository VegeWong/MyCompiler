package com.vegw.compiler.IR.LinearIR;

import com.vegw.compiler.IR.LinearIR.Operand.PhysicalRegister;
import com.vegw.compiler.IR.LinearIR.Operand.Register;

import java.util.LinkedList;
import java.util.List;

public class RegisterList {
    public List<Register> regs;
    public List<Register> paramRegs;
    public List<Register> calleeSavedRegs;

    public RegisterList() {
        com.vegw.compiler.IR.LinearIR.Operand.Register rax = PhysicalRegister.rax;
        com.vegw.compiler.IR.LinearIR.Operand.Register rbx = PhysicalRegister.rbx;
        com.vegw.compiler.IR.LinearIR.Operand.Register rcx = PhysicalRegister.rcx;
        com.vegw.compiler.IR.LinearIR.Operand.Register rdx = PhysicalRegister.rdx;
        com.vegw.compiler.IR.LinearIR.Operand.Register rsi = PhysicalRegister.rsi;
        com.vegw.compiler.IR.LinearIR.Operand.Register rdi = PhysicalRegister.rdi;
        com.vegw.compiler.IR.LinearIR.Operand.Register rbp = PhysicalRegister.rbp;
        com.vegw.compiler.IR.LinearIR.Operand.Register rsp = PhysicalRegister.rsp;
        com.vegw.compiler.IR.LinearIR.Operand.Register r8  = PhysicalRegister.r8;
        com.vegw.compiler.IR.LinearIR.Operand.Register r9  = PhysicalRegister.r9;
        com.vegw.compiler.IR.LinearIR.Operand.Register r10 = PhysicalRegister.r10;
        com.vegw.compiler.IR.LinearIR.Operand.Register r11 = PhysicalRegister.r11;
        com.vegw.compiler.IR.LinearIR.Operand.Register r12 = PhysicalRegister.r12;
        com.vegw.compiler.IR.LinearIR.Operand.Register r13 = PhysicalRegister.r13;
        com.vegw.compiler.IR.LinearIR.Operand.Register r14 = PhysicalRegister.r14;
        com.vegw.compiler.IR.LinearIR.Operand.Register r15 = PhysicalRegister.r15;

        regs = new LinkedList<Register>() {{
            add(rax);
            add(rbx);
            add(rcx);
            add(rdx);
            add(rsi);
            add(rdi);
            add(rbp);
            add(rsp);
            add(r8);
            add(r9);
            add(r10);
            add(r11);
            add(r12);
            add(r13);
            add(r14);
            add(r15);
        }};

        paramRegs = new LinkedList<Register>() {{
            add(rdi);
            add(rsi);
            add(rdx);
            add(rcx);
            add(r8);
            add(r9);
        }};

        calleeSavedRegs = new LinkedList<Register>() {{
            add(rbp);
            add(rbx);
            add(r12);
            add(r13);
            add(r14);
            add(r15);
        }};
    }
}
