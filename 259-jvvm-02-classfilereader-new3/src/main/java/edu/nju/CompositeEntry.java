package edu.nju;

import java.io.IOException;

/**
 * format : dir/subdir;dir/subdir/*;dir/target.jar*
 */
public class CompositeEntry extends Entry{
    public CompositeEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        byte[] ret = new byte[0];
        String[] allpath = classpath.split(PATH_SEPARATOR);
        for(int i = 0;i < allpath.length;++i){
            Entry fileEntry = ClassFileReader.chooseEntryType(allpath[i]);
            ret = fileEntry.readClassFile(className);
            if(ret != null){
                break;
            }
        }
        return ret;
    }
}
