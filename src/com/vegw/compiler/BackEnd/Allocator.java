package com.vegw.compiler.BackEnd;

import com.vegw.compiler.BackEnd.IRGenerator;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.IR.LinearIR.*;
import com.vegw.compiler.IR.LinearIR.Operand.Register;
import com.vegw.compiler.IR.LinearIR.Operand.VirtualRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Allocator {
    private IRGenerator irGenerator;
    private int noR = 4; //number of Physical Register
    private boolean[][] edge;
    private List<IRInstruction> insts;
    public Allocator(IRGenerator irGenerator) { this.irGenerator = irGenerator; }

    public void allocate() {
        for (FunctionEntity func : irGenerator.funcs) {
            allocateFunc(func);
        }
    }

    private void allocateFunc(FunctionEntity func) {
        boolean flag = true;
        IRInstruction inst;
        initialize(func);
        while (flag) {
            flag = false;
            for (int now = insts.size() - 1; now >= 0; --now) {
                inst = insts.get(now);
                flag |= check(inst);
            }
        }

        int n = func.idCnt;
        edge = new boolean[n][n];
        addEdge(func);
        color(n, func);

    }

    private boolean check(IRInstruction inst) {
        boolean flag = false;
        for (IRInstruction succ : inst.succ) {
            for (Register used : inst.use) {
                if (!inst.in.contains(used)) {
                    inst.in.add(used);
                    flag = true;
                }
            }
            for (Register out : succ.in) {
                if (!inst.out.contains(out)) {
                    inst.out.add(out);
                    flag = true;
                }
            }
            for (Register out : inst.out){
                if (!out.equals(inst.def) && !inst.in.contains(out)) {
                    inst.in.add(out);
                    flag = true;
                }
            }
        }
        return flag;
    }

    private void addEdge(FunctionEntity func) {
        for (IRInstruction inst : func.irInstructions) {
            if (inst instanceof Cjump || inst instanceof Jump)
                continue;
            if (inst.def == null) continue;
            for  (IRInstruction succ : inst.succ) {
                for (Register in : succ.in) {
                    addedge(inst.def, in);
                }
            }
        }
    }


    private void addedge(Register a, Register b) {
        if (a.id == -1 || b.id == -1) return;
        edge[a.id][b.id] = true;
        edge[b.id][a.id] = true;
    }

    private void initialize(FunctionEntity func) {
        insts= new ArrayList<IRInstruction>();
        for (IRInstruction ins : func.irInstructions) {
            if (ins instanceof Call) {
//                for (IRInstruction item : ((Call) ins).INS()) {
//                    insSet.add(item);
//                    if (pre != null) pre.next = item;
//                    pre = item;
//                }
//                continue;
            }
            insts.add(ins);
            if (ins instanceof Cjump) {
                insts.add(((Cjump) ins).cond);
            }
        }
    }

    private void color(int n, FunctionEntity func) {
        boolean[][] te = new boolean[n][n];
        boolean[] delete = new boolean[n];
        int[] conflict = new int[n];
        int[] useColor = new int[n];

        Stack<Register> stack= new Stack<Register>();
        int k = 0;

        for (int i = 0; i < n; ++i) useColor[i] = -1;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                te[i][j] = edge[i][j];

        boolean flag = true;
        int remain;
        while (flag) {
            flag = false;
            remain = 0;
            for (int i = 16; i < n; ++i) {
                if (delete[i]) continue;
                // Calculate neighbors
                ++remain;
                k = 0;
                for (int j = 16; j < n; ++j) {
                    if (i == j) continue;
                    if (te[i][j]) ++k;
                }
                conflict[i] = k;
                // Delete the node
                if (k < noR) {
                    stack.push(func.vrs.get(i - 16 + func.params().size()));
                    for (int j = 16; j < n; ++j) {
                        te[i][j] = false;
                        te[j][i] = false;
                    }
                    flag = true;
                    delete[i] = true;
                    conflict[i] = 0;
                }
            }

            // Find victim
            if (!flag && remain != 0) {
                flag = true;
                int indexOfMax = -1;
                int valOfMax = -1;

                for (int i = 16; i < n; ++i) {
                    if (delete[i]) continue;
                    if (valOfMax < conflict[i]) {
                        valOfMax = conflict[i];
                        indexOfMax = i;
                    }
                }

                for (int j = 16; j < n; ++j) {
                    te[indexOfMax][j] = false;
                    te[j][indexOfMax] = false;
                }
                delete[indexOfMax] = true;
                conflict[indexOfMax] = 0;
            }
        }

        // Color
        boolean[] c = new boolean[noR];
        while (!stack.empty()) {
            // Initialize
            c[0] = false;
            c[1] = false;
            c[2] = false;
            c[3] = false;

            Register reg = stack.pop();
            int nowId = reg.id;
            for (int i = 16; i < n; ++i) {
                if (nowId == i) continue;
                if (edge[i][nowId] && useColor[i] != -1) {
                    c[useColor[i]] = true;
                }
            }
            for (int i = 0; i < 4; ++i)
                if (!c[i]) {
                    useColor[nowId] = i;
                     ((VirtualRegister) reg).id = i + 12;
                }
        }
    }
}
