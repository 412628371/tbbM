package com.xinguang.tubobo.impl.wechat.aes;

import java.security.MessageDigest;
import java.util.Random;

public class WXSignUtils {

	private final static String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA1";
	
	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 *
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String nonceStr(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length())));
		}
		return sb.toString();
	}
	
	public static String encodeJSAPI(String str, String ncryptionAlg) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest digest = MessageDigest.getInstance(ncryptionAlg);
			digest.update(str.getBytes());
			
			byte[] bytes = digest.digest();
            int length = bytes.length;
            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < length; i++) {
                sb.append(HEX_DIGITS[(bytes[i] >>> 4) & 0x0f]);
                sb.append(HEX_DIGITS[bytes[i] & 0xf]);
            }

            return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		String a = "jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value";
		String b = "0f9de62fce790f9a083d5c99e95740ceb90c27ed";
		String c = encodeJSAPI(a, SHA1);
		System.out.println(c);
		System.out.println(c.equals(b));
	}
	
}
