package com.little.util;

/**
 * @author LittlePart
 * @version 1.0
 * @date 2020/04/2020/4/18 18:05
 */
public class ArgsResp {

    public static int syncFlag = GlobalConfig.ASYNC;

    public static int zeroCopyFlag = GlobalConfig.NOT_ZERO_COPY;

    public static int coreThreadCount = 32;

    public static int maxThreadCount = 1000;

    public static int maxQueueLength = Integer.MAX_VALUE;
}
