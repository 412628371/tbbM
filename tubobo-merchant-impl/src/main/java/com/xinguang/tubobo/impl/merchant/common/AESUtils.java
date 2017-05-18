package com.xinguang.tubobo.impl.merchant.common;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by shade on 2017/4/7.
 */
public class AESUtils {

    private static String PASSWORD = "eGluZ3Vhbmd0YmI=";
    private static String ENCODE = "UTF-8";
    private static String ALGORITHM = "AES/CBC/NoPadding";
    private static String IV = "svtpdprtrsjxabcd";

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @return
     */
    public static String encrypt(String content){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.getBytes(ENCODE);
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(PASSWORD.getBytes(ENCODE), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes(ENCODE));
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);// 初始化

            byte[] result = cipher.doFinal(plaintext);
            return new BASE64Encoder().encode(result); // 加密
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        }
        } catch (Exception e) {
            return "";
        }
    }

    /**解密
     * @param content  待解密内容
     * @return
     */
    public static String decrypt(String content){
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
            SecretKeySpec keyspec = new SecretKeySpec(PASSWORD.getBytes(ENCODE), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes(ENCODE));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] result = cipher.doFinal(new BASE64Decoder().decodeBuffer(content));
            return new String(result,ENCODE).trim(); // 加密
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        String content = "qqqq_1111";
        //加密
        String encryptResult = AESUtils.encrypt(content);
        System.out.println("加密后：" + encryptResult);
        //解密
        String decryptResult = AESUtils.decrypt(encryptResult);
//        String decryptResult = AESUtils.decrypt("m139kJciJRD/SOWbLVmyfg==");
        System.out.println("解密后：" + decryptResult);

//        System.out.println(StringUtils.equals(content, decryptResult));

        System.out.println(Base64.encode("xinguangtbb".getBytes("UTF-8")));

        System.out.println(getRandomString(16));
    }

    private static String string = "abcdefghijklmnopqrstuvwxyz";

    private static String getRandomString(int length){
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len-1)));
        }
        return sb.toString();
    }

    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }
}
