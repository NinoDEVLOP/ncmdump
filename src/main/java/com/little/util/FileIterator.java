package com.little.util;

import java.io.File;
import java.io.FileFilter;

/**
 * @author created by qingchuan.xia
 */
public interface FileIterator {

    /**
     * 遍历方法
     *
     * @param rootFile 开始遍历的根目录
     * @param filter   过滤制定特征的文件
     * @param runnable 当遍历到文件时,对文件执行的操作
     */
    void iterator(File rootFile, FileFilter filter, Executable<File> runnable);

}
