package edu.nju;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * format : dir/subdir/.../
 */
public class DirEntry extends Entry{
    private static final String FILE_SEPARATOR = File.separator;

    public DirEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        File dir = new File(classpath + FILE_SEPARATOR + className);
        if(dir.exists()){
            return IOUtil.readFileByBytes(new FileInputStream(dir));
        }
        return null;
    }
}
