package com.little.util;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author created by qingchuan.xia
 */
public class DefaultExecutable implements Executable<File> {

    private long duration = 0L;

    @Override
    public void execute(File file) {
        long start = System.currentTimeMillis();
        DefaultUnpackNcm unpackNcm = new DefaultUnpackNcm();
        try {
            unpackNcm.ncm2NormalFormat(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long cur = System.currentTimeMillis() - start;
        duration += cur;
        System.out.println("当前文件[" + file.getPath() + "]消耗时间为:[" + cur + "],合计消耗时间：[" +duration+ "]" );
    }
}
