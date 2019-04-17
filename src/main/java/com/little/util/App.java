package com.little.util;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static FileIterator fileIterator = new DefaultFileIterator();

    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            System.out.println("输入参数为空，请输入参数");
            return;
        }
        Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
        for (String arg : args) {
            File file = new File(arg);
            if (!file.exists()) {
                System.out.println("参数中相关文件未找到,请检查相关路径是否存在权限问题：" + file.getPath());
                continue;
            }
            iteratorFile2(file);
        }
    }

    private static void iteratorFile(File rootFile) throws Exception {
        FileFilter filter = pathname -> pathname.getName().endsWith(".ncm");
        SyncExecutable fileExecutable = new SyncExecutable(4);
//        DefaultExecutable fileExecutable = new DefaultExecutable();
        fileIterator.iterator(rootFile, filter, fileExecutable);
//        fileExecutable.executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    fileExecutable.executor.awaitTermination(fileExecutable.duration.get(), TimeUnit.MILLISECONDS);
//                    fileExecutable.executor.shutdown();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private static void iteratorFile2(File rootFile) throws Exception {
        FileFilter filter = pathname -> pathname.getName().endsWith(".ncm");
        DefaultExecutable fileExecutable = new DefaultExecutable();
        fileIterator.iterator(rootFile, filter, fileExecutable);
    }


}
