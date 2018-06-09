package com.vegw.compiler.BackEnd;

import com.vegw.compiler.AST.Expr.BinaryOpNode;
import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.IR.LinearIR.Assign;
import com.vegw.compiler.IR.LinearIR.Binop;
import com.vegw.compiler.IR.LinearIR.IRInstruction;

import java.util.LinkedList;
import java.util.List;

public class RedundantElimator {
    private IRGenerator irGenerator;
    private List<IRInstruction> irs;
    public RedundantElimator(IRGenerator irGenerator) {this.irGenerator = irGenerator;}

    public void elimate() {
        for (FunctionEntity func : irGenerator.funcs) {
            //elimateFunc(func);
        }
    }

    private void elimateFunc(FunctionEntity func) {
        irs = new LinkedList<IRInstruction>();
        List<IRInstruction> insts = func.irInstructions;
        for (int i = 0; i < insts.size(); ++i) {
            IRInstruction p = irs.isEmpty()? null : ((LinkedList<IRInstruction>) irs).getLast();
            IRInstruction n = insts.get(i);
            if (p instanceof Assign && n instanceof Assign) {
                if (((Assign) p).left == ((Assign) n).right) {
                    Assign assign = new Assign(((Assign) n).left, ((Assign) p).right);
                    ((LinkedList<IRInstruction>) irs).remove(p);
                    irs.add(assign);
                    continue;
                }
            }
            irs.add(n);
        }
        func.irInstructions =  irs;
    }
}
