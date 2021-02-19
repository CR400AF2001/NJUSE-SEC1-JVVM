package edu.nju;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * format : dir/.../*
 */
public class WildEntry extends Entry{
    public WildEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        File dir = new File(classpath.substring(0,classpath.length() - 2));
        if(!dir.exists()){
            return null;
        }
        File[] dirlists = dir.listFiles();
        assert dirlists != null;
        String newclasspath = "";
        for(int i = 0; i < dirlists.length;++i){
            if(dirlists[i].getName().substring(dirlists[i].getName().length() - 4).toLowerCase().equals(".jar")||dirlists[i].getName().substring(dirlists[i].getName().length() - 4).toLowerCase().equals(".zip")){
                newclasspath += dirlists[i].getPath();
                newclasspath += PATH_SEPARATOR;
            }
        }
        if(newclasspath == ""){
            return null;
        }
        CompositeEntry entry = new CompositeEntry(newclasspath.substring(0,newclasspath.length() - 1));
        return entry.readClassFile(className);
    }
}
