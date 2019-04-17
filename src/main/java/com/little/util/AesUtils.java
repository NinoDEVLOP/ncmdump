package com.little.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {

    public static byte[] decrypt(byte[] key, byte[] content) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化为解密模式的密码器
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
