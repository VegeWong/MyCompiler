package com.vegw.compiler.IR.LinearIR.Operand;

public class VirtualRegister extends Register {
    public int cnt = -1;
    public int id = -1;
    // If the virtual register is an copy of value of other virtual register before computing
    public int color = -1;
    public VirtualRegister(int addrCnt, int idCnt) {
        super.inReg = false;
        this.cnt = addrCnt;
        this.id = idCnt;
    }


    @Override
    public String toNASM() {
        switch (color) {
            case -1: return "qword [rbp-" + cnt*8 + "]";
            case 0: return "r12";
            case 1: return "r13";
            case 2: return "r14";
            default: return "r15";
        }
    }

    public String toNASM(boolean b) {
        return "qword [rbp-" + cnt*8 + "]";
    }

    @Override
    public int id() {
        return id;
    }
}
