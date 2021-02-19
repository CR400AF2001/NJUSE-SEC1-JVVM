package com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.info.MethodrefInfo;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

@Getter
@Setter
public class MethodRef extends MemberRef {
    private Method method;

    public MethodRef(RuntimeConstantPool runtimeConstantPool, MethodrefInfo methodrefInfo) {
        super(runtimeConstantPool, methodrefInfo);
    }

    /**
     * TODO：实现这个方法
     * 这个方法用来实现对象方法的动态查找
     * @param clazz 对象的引用
     */
    public Method resolveMethodRef(JClass clazz) {
        method = resolveMethod(clazz);
        return method;
    }

    /**
     * TODO: 实现这个方法
     * 这个方法用来解析methodRef对应的方法
     * 与上面的动态查找相比，这里的查找始终是从这个Ref对应的class开始查找的
     */
    public Method resolveMethodRef() {
        if(method != null){
            return method;
        }
        else{
            try {
                resolveClassRef();
                method = resolveMethod(clazz);
                return method;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Method resolveMethod(JClass clazz){
        Optional<Method> optionalMethod;
        JClass currentClazz = clazz;
        while(currentClazz != null){
            optionalMethod = currentClazz.resolveMethod(name,descriptor);
            if(optionalMethod.isPresent()){
                return optionalMethod.get();
            }
            currentClazz = currentClazz.getSuperClass();
        }
        JClass[] allInterfaces = clazz.getInterfaces();
        int length = allInterfaces.length;
        JClass[] newInterfaces = null;
        allInterfaces = ArrayUtils.addAll(allInterfaces,allInterfaces);
        for(int i = 0; i < length; ++i){
            optionalMethod = allInterfaces[i].resolveMethod(name,descriptor);
            if(optionalMethod.isPresent()){
                return optionalMethod.get();
            }
            newInterfaces = allInterfaces[i].getInterfaces();
            if(newInterfaces != null){
                allInterfaces = ArrayUtils.addAll(allInterfaces,newInterfaces);
                length = allInterfaces.length;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "MethodRef to " + className;
    }
}
