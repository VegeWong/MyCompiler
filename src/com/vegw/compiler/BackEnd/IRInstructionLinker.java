package com.vegw.compiler.BackEnd;

import com.vegw.compiler.Entity.FunctionEntity;
import com.vegw.compiler.IR.LinearIR.*;

import java.util.Iterator;
import java.util.List;

public class IRInstructionLinker{
    IRGenerator irGenerator;

    public IRInstructionLinker(IRGenerator irGenerator) { this.irGenerator = irGenerator; }

    public void link() {
        for (FunctionEntity func : irGenerator.funcs)
            linkFunc(func);
    }

    private void linkFunc(FunctionEntity func) {
        List<IRInstruction> insts = func.irInstructions;
        Iterator<IRInstruction> itr = insts.iterator();

        IRInstruction nowIns = null;
        IRInstruction nxtIns = itr.hasNext()? itr.next() : null;

        int cnt = 0;
        boolean flag = true;
        while (flag) {
            flag = false;
            while (true) {
                nowIns = nxtIns;
                nxtIns = itr.hasNext() ? itr.next() : null;
                if (nowIns == null || nxtIns == null) break;
                if (cnt > 0) {
                    if (nowIns instanceof Cjump) {
                        for (IRInstruction pred : nowIns.pred) {
                            pred.succ.remove(nowIns);
                            nowIns.pred.remove(pred);
                            flag |= linkIns(pred, ((Cjump) nowIns).cond);
                            if (((Cjump) nowIns).thenLabel != null)
                                flag |= linkIns(((Cjump) nowIns).cond, ((Cjump) nowIns).thenLabel);
                            if (((Cjump) nxtIns).elseLabel != null)
                                flag |= linkIns(((Cjump) nowIns).cond, ((Cjump) nowIns).elseLabel);
                        }
                    } else if (nowIns instanceof Jump) {
                        for (IRInstruction pred : nowIns.pred) {
                            pred.succ.remove(nowIns);
                            nowIns.pred.remove(pred);
                            flag |= linkIns(pred, ((Jump) nowIns).target);
                        }
                    } else if (nowIns instanceof Label) {
                        for (IRInstruction pred : nowIns.pred) {
                            pred.succ.remove(nowIns);
                            nowIns.pred.remove(pred);
                            flag |= linkIns(pred, nxtIns);
                        }
                    }
                }
                else
                    linkIns(nowIns, nxtIns);
            }
            ++cnt;
        }
    }

    private boolean linkIns(IRInstruction now, IRInstruction suc) {
        if (!now.succ.contains(suc))
            now.addSucc(suc);
        if (!suc.pred.contains(now))
            suc.addPred(now);
        return true;
    }

}
