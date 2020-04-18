package com.little.util;

import java.io.File;
import java.io.IOException;

/**
 * @author LittlePart
 * @version 1.0
 * @date 2020/04/2020/4/16 12:36
 */
public class FileRename {

    public static void main(String[] args) throws IOException {
        File file = new File("｜");
        System.out.println(file.renameTo(new File("jkdal＊")));
    }

}
