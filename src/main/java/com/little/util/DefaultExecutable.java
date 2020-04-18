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
public class DefaultExecutable implements Executable<File> {

    public AtomicLong startTime;
    public ThreadPoolExecutor executor;
    private boolean asyncFlag;

    public DefaultExecutable(int asyncFlag) {
        //26m文件 处理100次  机器运行 4核  8个逻辑处理器
        //单线程处理，消耗时间    25s
        //64个线程处理，消耗时间: 23s
        //32个线程处理，消耗时间: 15s左右
        //16个线程处理，消耗时间: 20s左右
        this.asyncFlag = asyncFlag == GlobalConfig.ASYNC;
        this.startTime = new AtomicLong(System.currentTimeMillis());
        if (this.asyncFlag) {
            executor = new ThreadPoolExecutor(
                    ArgsResp.coreThreadCount,
                    ArgsResp.maxThreadCount, 1000, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(ArgsResp.maxQueueLength));
        }
    }

    @Override
    public void exec(final File file) {
        handle(asyncFlag, file);
    }

    private void handle(boolean async, File file) {
        Runnable runnable = () -> {
            Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING);
            DefaultUnpackNcm unpackNcm = new DefaultUnpackNcm(ArgsResp.zeroCopyFlag);
            try {
                unpackNcm.ncm2NormalFormat(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long cur = System.currentTimeMillis() - startTime.get();
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + "当前文件[" + file.getPath() + "],合计消耗时间:[" + cur + "]ms");

        };
        if (async) {
            executor.execute(runnable);
        } else {
            runnable.run();
        }
    }


}
