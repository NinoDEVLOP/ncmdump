package com.little.util;

import java.io.File;
import java.io.FileFilter;

/**
 * @author created by qingchuan.xia
 */
public class DefaultFileIterator implements FileIterator {
    @Override
    public void iterator(File rootFile, final FileFilter filter, Executable<File> executable) {
        if (!rootFile.isDirectory()){
            executable.execute(rootFile);
            return;
        }

        File[] files = rootFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || filter.accept(pathname);
            }
        });

        if (files != null && files.length > 0){
            for (File file : files){
                iterator(file,filter,executable);
            }
        }
    }
}
