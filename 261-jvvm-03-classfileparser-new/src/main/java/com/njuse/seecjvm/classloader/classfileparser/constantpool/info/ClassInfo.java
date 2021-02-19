package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.other.UTF8;


public class ClassInfo extends ConstantPoolInfo {


    /**
     * Add some codes here.
     *
     * tips:
     * step1
     *      ClassInfo need a private field, what is it?
     * step2
     *      You need to add some args in constructor
     *      and don't forget to set tag
     *
     *      super method and super key word will help you
     */

    //todo attibute of ClassInfo
    private int nameIndex;

    //todo constructor of ClassInfo
    public ClassInfo(ConstantPool constantPool, int nameIndex){
        super(constantPool);
        this.nameIndex = nameIndex;
        super.tag = ConstantPoolInfo.CLASS;
    }

    //todo getClassName
    public String getClassName() {
        /**
         * Add some codes here.
         * tips: classname is in UTF8Info
         */
        return ((UTF8Info)myCP.get(nameIndex)).getString();
    }

    @Override
    public String toString() {

        return getClassName();
    }
}
