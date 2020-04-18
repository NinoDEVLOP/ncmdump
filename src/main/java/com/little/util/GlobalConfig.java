package com.little.util;

/**
 * @author LittlePart
 * @version 1.0
 * @date 2020/04/2020/4/18 16:24
 */
public class GlobalConfig {

    /**
     * 经过测试不用MappedByteBuffer会相对快一些
     */
    public static final int ZERO_COPY = 1;

    public static final int NOT_ZERO_COPY = 0;

    public static final int ASYNC = 1;

    public static final int SYNC = 0;



}
