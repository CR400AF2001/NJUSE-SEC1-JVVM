package edu.nju;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * format : dir/subdir/target.jar
 */
public class ArchivedEntry extends Entry{
    public ArchivedEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        ZipFile zipFile = new ZipFile(classpath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()){
            ZipEntry zipEntry = entries.nextElement();
            String Name = IOUtil.transform(zipEntry.getName());
            if(Name.equals(className)){
                return IOUtil.readFileByBytes(zipFile.getInputStream(zipEntry));
            }
        }
        return null;
    }
}
