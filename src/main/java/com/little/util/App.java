package com.little.util;

import java.io.File;
import java.io.FileFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 本地测试JVM启动参数
 * -server -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:./gc.log -Xms3072M -Xmx3072M -Xmn1536M -XX:MetaspaceSize=16M -XX:MaxMetaspaceSize=16M -XX:+UseConcMarkSweepGC
 * -XX:MaxDirectMemorySize=1536M
 */
public class App {

    private static FileIterator fileIterator = new DefaultFileIterator();

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            args = new String[]{"./"};
            System.out.println("未指定文件目录，将转换当前目录以及子目录下ncm文件");
        }
        try {
            initArgsResp(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("请检查输入参数是否正确");
            System.err.println("示例:");
            System.err.println("java -jar -async -c 15 ./");
            return;
        }
        long start = System.currentTimeMillis();
        Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
        for (String arg : args) {
            if (arg.charAt(0) == '-') {
                continue;
            }
            File file = new File(arg);
            if (!file.exists()) {
                //System.out.println("参数中相关文件未找到,请检查相关路径是否存在权限问题：" + file.getPath());
                continue;
            }
            iteratorFile(file);
        }
    }

    private static void initArgsResp(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) != '-') {
                continue;
            }
            switch (args[i]) {
                case "-zerocopy":
                case "-zc":
                    ArgsResp.zeroCopyFlag = GlobalConfig.ZERO_COPY;
                    break;
                case "-nozerocopy":
                case "-nzc":
                    ArgsResp.zeroCopyFlag = GlobalConfig.NOT_ZERO_COPY;
                    break;
                case "-s":
                case "-sync":
                    ArgsResp.syncFlag = GlobalConfig.SYNC;
                    break;
                case "-a":
                case "-async":
                    ArgsResp.syncFlag = GlobalConfig.ASYNC;
                    break;

                case "-c":
                case "-corethread":
                    ArgsResp.coreThreadCount = Integer.parseInt(args[i + 1]);
                    i += 1;
                    break;
                case "-mt":
                case "-maxthread":
                    ArgsResp.maxThreadCount = Integer.parseInt(args[i + 1]);
                    i += 1;
                    break;
                case "-ql":
                case "-queuelength":
                    ArgsResp.maxQueueLength = Integer.parseInt(args[i + 1]);
                    i += 1;
                    break;
                case "-h":
                case "-help":

                default:
                    break;

            }
        }


    }

    private static void iteratorFile(File rootFile) {
        FileFilter filter = pathname -> pathname.getName().endsWith(".ncm");
        Executable<File> executable = new DefaultExecutable(ArgsResp.syncFlag);
        fileIterator.iterator(rootFile, filter, executable);
    }


}
