package com.little.util;

import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author LittlePart
 * @version 1.0
 * @date 2020/04/2020/4/15 11:05
 */
public class ZerocopyRepeatReadStream extends RepeatReadStream {

    private FileInputStream fis = null;

    private FileChannel fc = null;

    private MappedByteBuffer mbb = null;

    private File file;

    private int position = 0;

    @Override
    public void outPutFile(File targetFile, byte[] fileData) {
        RandomAccessFile stream = null;
        FileChannel fc = null;
        MappedByteBuffer mbb = null;
        try {
            if (!targetFile.exists() && !targetFile.createNewFile()) {
                throw new RuntimeException("can't create file that save byte data");
            }
            stream = new RandomAccessFile(targetFile,"rw");
            fc = stream.getChannel();
            mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fileData.length);
            mbb.put(fileData);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mbb != null) {
                mbb.force();
                Cleaner cl = ((DirectBuffer) mbb).cleaner();
                if (cl != null) {
                    cl.clean();
                }
            }
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                }
            }

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public void setFile(File sourceFile) throws FileNotFoundException, IOException {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            throw new FileNotFoundException("File is can not readable! please");
        }
        if (fis == null) {
            fis = new FileInputStream(sourceFile);
            fc = fis.getChannel();
            mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sourceFile.length());
        }
        file = sourceFile;
    }

    @Override
    public ValidByte readFile(int length) throws IOException {
        if (fis == null) {
            throw new IllegalArgumentException("fileStream has not been init,check logic!");
        }
        ValidByte result = new ValidByte(length);
        try {
            mbb.get(result.data, 0, length);
            position += length;
        } catch (Exception e) {
            e.printStackTrace();
            this.close();
        }
        return result;
    }

    @Override
    public ValidByte readFile() {
        if (fis == null) {
            throw new IllegalArgumentException("fileStream has not been init,check logic!");
        }
        byte[] totalData = new byte[(int) file.length() - position];
        try {
            mbb.get(totalData);
            ValidByte validByte = new ValidByte(totalData.length);
            validByte.data = totalData;
            return validByte;
        } finally {
            this.close();
        }
    }

    @Override
    public void skip(int offset) {
        position += offset;
        mbb.position(position);
    }

    @Override
    public void close() {
        if (mbb != null) {
            mbb.force();
            Cleaner cl = ((DirectBuffer) mbb).cleaner();
            if (cl != null) {
                cl.clean();
            }
        }
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {
            }
        }

        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
            }
        }
        mbb = null;
        fc = null;
        fis = null;
    }


}
