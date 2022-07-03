package com.huang.utils.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MD5Util {

	// 生成MD5
	public static String getMD5(String message) throws Exception {
		String md5 = "";
		MessageDigest md = MessageDigest.getInstance("MD5"); // 创建一个md5算法对象
		byte[] messageByte = message.getBytes(StandardCharsets.UTF_8);
		byte[] md5Byte = md.digest(messageByte); // 获得MD5字节数组,16*8=128位
		md5 = bytesToHex(md5Byte); // 转换为16进制字符串
		return md5;
	}

	// 二进制转十六进制
	private static String bytesToHex(byte[] bytes) throws Exception {
		StringBuffer hexStr = new StringBuffer();
		int num;
		for (byte aByte : bytes) {
			num = aByte;
			if (num < 0) {
				num += 256;
			}
			if (num < 16) {
				hexStr.append("0");
			}
			hexStr.append(Integer.toHexString(num));
		}
		return hexStr.toString().toUpperCase();
	}
}