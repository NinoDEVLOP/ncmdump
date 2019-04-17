package com.little.util;

import java.nio.ByteBuffer;

/**
 * @author created by qingchuan.xia
 */
public class ByteUtil {

    /**
     * 16进制的字符串转为byte数组
     */
    public static byte[] hexStr2Byte(String hex) {
        ByteBuffer bf = ByteBuffer.allocate(hex.length() / 2);
        for (int i = 0; i < hex.length(); i++) {
            String hexStr = hex.charAt(i) + "";
            i++;
            hexStr += hex.charAt(i);
            byte b = (byte) Integer.parseInt(hexStr, 16);
            bf.put(b);
        }
        return bf.array();
    }

    /**
     * 返回一个从0到255填充的byte[]
     */
    public static byte[] fillByteArr() {
        byte b = 0;
        byte [] arr = new byte[256];
        for (int i = 0; i <= 0xFF; i++) {
            arr[i] = b;
            b++;
        }
        return arr;
    }

    /**
     * 将长度为4的byte数组转为int类型
     */
    public static int bytes2Int(byte[] byteNum) {
        int num = 0;
        for (byte b : byteNum) {
            num <<= 8;
            num |= (b & 0xff);
        }
        return num;
    }

    /**
     * 将byte[]翻转
     */
    public static byte[] reverse(byte[] param) {
        int i = 0;
        int j = param.length - 1;
        while (i < j) {
            byte tmp = param[i];
            param[i] = param[j];
            param[j] = tmp;
            i++;
            j--;
        }
        return param;
    }

    /**
     * 作用和python的stack.unpack('<1',bytes([1,2,3,4]))方法一致
     */
    public static int unpack(byte[] bytes){
        return bytes2Int(reverse(bytes));
    }

}
