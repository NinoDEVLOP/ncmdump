package com.little.util;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author created by qingchuan.xia
 */
public class SyncExecutable implements Executable<File> {

    public AtomicLong duration = new AtomicLong();
    public ThreadPoolExecutor executor;

    public SyncExecutable(int maxThread) {
        executor = new ThreadPoolExecutor(4, maxThread, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    public void execute(final File file) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
                long start = System.currentTimeMillis();
                DefaultUnpackNcm unpackNcm = new DefaultUnpackNcm();
                try {
                    unpackNcm.ncm2NormalFormat(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                long cur = System.currentTimeMillis() - start;
                duration.addAndGet(System.currentTimeMillis() - start);
                System.out.println(Thread.currentThread().getName() + "当前文件[" + file.getPath() + "]消耗时间为:[" + cur + "],合计消耗时间：[" +duration.get()+ "]" );
            }
        };
        executor.execute(runnable);
    }
}
