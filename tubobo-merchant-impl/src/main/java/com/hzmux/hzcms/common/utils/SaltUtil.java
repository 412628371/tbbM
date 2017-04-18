package com.hzmux.hzcms.common.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class SaltUtil {

    /**
     * 生成随机数的字符库
     */
    private static byte[] charLibs = new byte[62];

    /**
     * 初始化字符库数组
     */
    static {
        for (int i = 'a'; i <= 'z'; i++) {
            charLibs[i - 'a'] = (byte) i;
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            charLibs[i - 'A' + 26] = (byte) i;
        }
        for (int i = '0'; i <= '9'; i++) {
            charLibs[i - '0' + 52] = (byte) i;
        }
    }

    /**
     * 加盐操作<br/>
     * 输入的是"纯净"的password，返回的是一个数组<br/>
     * 元素0：存入数据库的最终加密password<br/>
     * 元素1：盐值
     * 
     * @param password
     * @return
     */
    public static String[] encryptSalt(String password) {

        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssSSS");
        StringBuilder s = new StringBuilder();
        for (int j = 0; j < 11; j++) {
            int k = random.nextInt(charLibs.length);
            s.append((char) charLibs[k]);
        }
        /** 盐值 */
        String salt = s.toString() + sdf.format(new Date());
        String[] keyArray = new String[2];
        /** 加密最终的密码 */
        keyArray[0] = DigestUtils.sha1Hex(password + salt);
        keyArray[1] = salt;
        return keyArray;
    }

    /**
     * 判断密码是否正确<br/>
     * 场景：登录或支付。
     * 
     * @param password
     *            用户输入的原始密码(如果client->Server过程中有加密，则需要先解密，再使用)
     * @param salt
     *            该用户的盐值
     * @param encryptedPassword
     *            该用户数据表中的加密后的最终密码
     * @return
     */
    public static boolean judgePassword(String password, String salt,
            String encryptedPassword) {

        if (StringUtils.isNotBlank(encryptedPassword)
                && StringUtils.isNotBlank(salt) && StringUtils.isNotBlank(password)) {
            return DigestUtils.sha1Hex(password.trim() + salt.trim()).equals(
                    encryptedPassword.trim()) ? true : false;
        } else {
            return false;
        }
    }
    

    public static void main(String[] args) {
        String ysmm = "hanyong";
        String ppmm = "admin";
        String[] testArray = SaltUtil.encryptSalt(ysmm);
        System.out.println("用户输入的密码：" + ysmm);
        System.out.println("最终密码：" + testArray[0]);
        System.out.println("盐值：" + testArray[1]);

        SaltUtil.judgePassword(ysmm, testArray[1], testArray[0]);
        System.out.println("输入密码(" + ppmm + "),匹配结果是："
                + SaltUtil.judgePassword(ppmm, testArray[1], testArray[0]));

    }
    
}
