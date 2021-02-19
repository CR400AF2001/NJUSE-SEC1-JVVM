package com.njuse.seecjvm.instructions.comparison;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

public class DCMPG extends NoOperandsInstruction {

    /**
     * TODO：实现这条指令
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        double val2 = stack.popDouble();
        double val1 = stack.popDouble();
        int res = 0;
        double cha = Math.abs(val1 - val2);
        if(Double.isNaN(val1) || Double.isNaN(val2)){
            res = 1;
        }
        else if(cha < 10e-8){
            res = 0;
        }
        else if(val1 > val2){
            res = 1;
        }
        else if(val1 < val2){
            res = -1;
        }
        stack.pushInt(res);
    }
}
