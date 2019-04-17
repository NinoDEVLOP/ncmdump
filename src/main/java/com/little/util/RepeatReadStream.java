package com.little.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class RepeatReadStream {

    /**
     * 会读多次
     */
    private FileInputStream fis = null;

    private File file;

    /**
     * 文件字节输出
     */
    public void outPutFile(File targetFile, byte[] fileData) {

        FileOutputStream stream = null;

        try {
            if (!targetFile.exists() && !targetFile.createNewFile()){
                throw new RuntimeException("can't create file that save byte data");
            }
            stream = new FileOutputStream(targetFile);
            stream.write(fileData);
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setFile(File sourceFile) throws FileNotFoundException {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new FileNotFoundException("File is can not readable! please");
        }
        if (fis == null) {
            fis = new FileInputStream(sourceFile);
        }
        file = sourceFile;
    }

    public ValidByte readFile(int length) throws IOException {
        if (fis == null) {
            throw new IllegalArgumentException("fileStream has not been init,check logic!");
        }
        ValidByte result = new ValidByte(length);
        try {
            result.length = fis.read(result.data);
        } catch (Exception e) {
            e.printStackTrace();
            fis.close();
        } finally {
            if (result.length == -1) {
                fis.close();
                fis = null;
            }
        }
        return result;
    }

    public ValidByte readFile() {
        if (fis == null) {
            throw new IllegalArgumentException("fileStream has not been init,check logic!");
        }
        byte[] totalData = new byte[(int) file.length()];
        try {
            int length = fis.read(totalData);
            ValidByte validByte = new ValidByte(length);
            validByte.data = totalData;
            return validByte;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ValidByte(0);
    }

    public void skip(int offset) {
        if (fis != null) {
            try {
                fis.skip(offset);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void close() {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static class ValidByte {
        public int length;
        public byte[] data;

        public ValidByte() {
            this(0);
        }

        public ValidByte(int length) {
        	this.length = length;
            data = new byte[length];
        }

        public byte[] getValidByte() {
            return length == 0 ? new byte[0] : Arrays.copyOfRange(data, 0, length);
        }
    }
}
