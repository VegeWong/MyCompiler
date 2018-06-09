package com.vegw.compiler.IR.LinearIR.Operand;

public class VirtualRegister extends Register {
    public int cnt = -1;
    public VirtualRegister(int addrCnt, int idCnt) {
        super.id = idCnt;
        this.cnt = addrCnt;
    }


    @Override
    public String toNASM() {
       switch (super.id) {
           case 0: return "rax" ;
           case 1: return "rbx" ;
           case 2: return "rcx" ;
           case 3: return "rdx" ;
           case 4: return "rsi" ;
           case 5: return "rdi" ;
           case 6: return  "rbp" ;
           case 7: return "rsp" ;
           case 8: return "r8" ;
           case 9: return "r9" ;
           case 10: return "r10" ;
           case 11: return "r11" ;
           case 12: return "r12" ;
           case 13: return "r13" ;
           case 14: return "r14" ;
           case 15: return "r15";
           default: System.err.println("Virtual Register address required"); return cnt+id+"None";
       }
    }
}
