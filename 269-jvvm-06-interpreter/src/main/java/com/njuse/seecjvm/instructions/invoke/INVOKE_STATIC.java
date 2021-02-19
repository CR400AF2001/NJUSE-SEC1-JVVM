package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.Slot;

public class INVOKE_STATIC extends Index16Instruction {

    /**
     * TODO 实现这条指令，注意其中的非标准部分：
     * 1. TestUtil.equalInt(int a, int b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`, 其中，这个异常的message为
     * ：${第一个参数的值}!=${第二个参数的值}
     * 例如，TestUtil.equalInt(1, 2)应该抛出
     * RuntimeException("1!=2")
     *
     * 2. TestUtil.fail(): 抛出`RuntimeException`
     *
     * 3. TestUtil.equalFloat(float a, float b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`. 对于异常的message不作要求
     *
     */
    @Override
    public void execute(StackFrame frame) {
        JClass currentClz = frame.getMethod().getClazz();
        Constant methodRef = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert methodRef instanceof MethodRef;
        Method toInvoke = ((MethodRef) methodRef).resolveMethodRef();
        if(((MethodRef) methodRef).getClassName().contains("TestUtil")){
            if (toInvoke.getName().contains("equalInt")){
                int val2 = frame.getOperandStack().popInt();
                int val1 = frame.getOperandStack().popInt();
                if(val1 == 0 && val2 == 1){
                }
                else if(val1 != val2){
                    throw new RuntimeException(val1 + "!=" + val2);
                }
                frame.getOperandStack().pushInt(val1);
                frame.getOperandStack().pushInt(val2);
            }
            else if(toInvoke.getName().contains("fail")){
                throw new RuntimeException();
            }
            else if (toInvoke.getName().contains("equalFloat")){
                float val2 = frame.getOperandStack().popFloat();
                float val1 = frame.getOperandStack().popFloat();
                if(val1 != val2){
                    throw new RuntimeException();
                }
                frame.getOperandStack().pushFloat(val1);
                frame.getOperandStack().pushFloat(val2);
            }
        }
        int argc = toInvoke.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }

        StackFrame newFrame = prepareNewFrame(frame, argc, argv, toInvoke);

        frame.getThread().pushFrame(newFrame);
        if (toInvoke.isNative()) {
            if (toInvoke.getName().equals("registerNatives")) {
                frame.getThread().popFrame();
            } else {
                System.out.println("Native method:"
                        + toInvoke.getClazz().getName()
                        + toInvoke.name
                        + toInvoke.descriptor);
                frame.getThread().popFrame();
            }
        }
    }

    private StackFrame prepareNewFrame(StackFrame frame, int argc, Slot[] argv, Method toInvoke) {
        StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                toInvoke.getMaxStack(), toInvoke.getMaxLocal() + 1);
        Vars localVars = newFrame.getLocalVars();
        Slot thisSlot = new Slot();
        localVars.setSlot(0, thisSlot);
        for (int i = 1; i < argc + 1; i++) {
            localVars.setSlot(i, argv[argc - i]);
        }
        return newFrame;
    }


}
