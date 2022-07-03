package com.huang.utils.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author HuangShen
 * @Description AES 加解密 工具类
 * @create 2021-10-15 8:34
 */
public class AESUtils {


    // 默认全部使用ECB模式的对称加密(主要是因为ECB模块适合并行计算)
    private static final String ALGORITHM_PADDING = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM_NAME = "AES";

    static {
        try {
            KeyGenerator sKeyGen = KeyGenerator.getInstance(ALGORITHM_NAME);
            sKeyGen.init(128);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //加密方法
    private static byte[] encrypt(String content, byte[] password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password, ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(byteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //解密方法
    private static byte[] decrypt(byte[] content, byte[] password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password, ALGORITHM_NAME);
            Cipher cipher = Cipher.getInstance(ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转换为二进制字节数组
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr == null || hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
                    16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 二进制字节数组转换为字符串
     *
     * @param bytes
     * @return
     */
    public static String byteArray2HexString(byte[] bytes) {
        if (null == bytes) {
            return "";
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(0xFF & b);
            while (str.length() < 2) {
                str = "0" + str;
            }
            hexString.append(str);
        }
        return hexString.toString();
    }

    //加密主方法
    public static String encode(String content, byte[] key) {
        return byteArray2HexString(encrypt(content, key));
    }

    //解密主方法
    public static String decode(String content, byte[] key) {
        byte[] b = decrypt(parseHexStr2Byte(content), key);
        return new String(b);
    }
}