package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;


public class PUTFIELD extends Index16Instruction {
    /**
     * TODO 实现这条指令
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        Method currentMethod = frame.getMethod();
        JClass currentClazz = currentMethod.getClazz();
        RuntimeConstantPool currentRuntimeConstantPool = currentClazz.getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) currentRuntimeConstantPool.getConstant(index);
        Field field;
        try{
            field = fieldRef.getResolvedFieldRef();
            if(field.isStatic()){
                throw new IncompatibleClassChangeError();
            }
            if(field.isFinal()){
                if(field.getClazz() != currentClazz || !currentMethod.getName().equals("<init>")){
                    throw new IllegalAccessError();
                }
            }
            OperandStack stack = frame.getOperandStack();
            String descriptor = field.getDescriptor();
            int slotID = field.getSlotID();
            JObject objectRef;
            switch (descriptor.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    int intValue = stack.popInt();
                    objectRef = stack.popObjectRef();
                    ((NonArrayObject) objectRef).getFields().setInt(slotID,intValue);
                    break;
                case 'F':
                    float floatValue = stack.popFloat();
                    objectRef = stack.popObjectRef();
                    ((NonArrayObject) objectRef).getFields().setFloat(slotID,floatValue);
                    break;
                case 'J':
                    long longValue = stack.popLong();
                    objectRef = stack.popObjectRef();
                    ((NonArrayObject) objectRef).getFields().setLong(slotID,longValue);
                    break;
                case 'D':
                    double doubleValue = stack.popLong();
                    objectRef = stack.popObjectRef();
                    ((NonArrayObject) objectRef).getFields().setDouble(slotID,doubleValue);
                    break;
                case 'L':
                case '[':
                    JObject ObjectRef = stack.popObjectRef();
                    objectRef = stack.popObjectRef();
                    ((NonArrayObject) objectRef).getFields().setObjectRef(slotID,ObjectRef);
                    break;
                default:
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
