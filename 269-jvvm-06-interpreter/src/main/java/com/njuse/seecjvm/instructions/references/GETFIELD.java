package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;

public class GETFIELD extends Index16Instruction {

    /**
     * TODO 实现这条指令
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        RuntimeConstantPool runtimeConstantPool = frame.getMethod().getClazz().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        Field field;
        try {
            field = fieldRef.getResolvedFieldRef();
            if (field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }
            OperandStack stack = frame.getOperandStack();
            NonArrayObject objectRef = (NonArrayObject)stack.popObjectRef();
            if(objectRef.isNull()){
                throw new NullPointerException();
            }
            String descriptor = field.getDescriptor();
            int slotID = field.getSlotID();
            Vars filedVars = objectRef.getFields();
            switch (descriptor.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    stack.pushInt(filedVars.getInt(slotID));
                    break;
                case 'F':
                    stack.pushFloat(filedVars.getFloat(slotID));
                    break;
                case 'J':
                    stack.pushLong(filedVars.getLong(slotID));
                    break;
                case 'D':
                    stack.pushDouble(filedVars.getDouble(slotID));
                    break;
                case 'L':
                case '[':
                    stack.pushObjectRef(filedVars.getObjectRef(slotID));
                    break;
                default:
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
