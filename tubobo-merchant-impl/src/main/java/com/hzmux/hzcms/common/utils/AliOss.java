package com.hzmux.hzcms.common.utils;

import com.aliyun.oss.OSSClient;
import com.baidu.disconf.client.usertools.DisconfDataGetter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;

import java.net.URL;
import java.util.Date;

/**
 * Created by ou_young on 2017/4/25.
 */
public class AliOss {

    private static String endpoint;
    private static String accessKeyId;
    private static String accessKeySecret;

    public static String BUCKETNAME_PUBLIC;
    public static String BUCKETNAME_PRIVATE;

    private static OSSClient ossClient;

    static{

        endpoint = (String) DisconfDataGetter.getByFileItem("common.properties", "aliyun.oss.endpoint");
        accessKeyId = (String) DisconfDataGetter.getByFileItem("common.properties", "aliyun.oss.accessKeyId");
        accessKeySecret = (String) DisconfDataGetter.getByFileItem("common.properties", "aliyun.oss.accessKeySecret");
        BUCKETNAME_PUBLIC = (String) DisconfDataGetter.getByFileItem("common.properties", "aliyun.oss.bucket.public");
        BUCKETNAME_PRIVATE = (String) DisconfDataGetter.getByFileItem("common.properties", "aliyun.oss.bucket.private");

        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

    }

    /**
     * 获取签名后的访问url (默认过期时间1小时)
     *
     * @param key
     * @param bucketName
     * @return
     */
    public static String generateSignedUrl(String key, String bucketName) {
        if (key.startsWith("http://")) {
            key = key.split("\\?")[0];
            key = key.split("com/")[1];
        }
        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        return url.toString();
    }

    /**
     * 获取访问url (需bucket属性为公共读)
     *
     * @param key
     * @param bucketName
     * @return
     */
    public static String generateUrl(String key, String bucketName) {
        if (key.startsWith("http://")) return key;
        return "http://" + bucketName + "." + endpoint + "/" + key;
    }

    public static String subAliossUrl(String url) {
        if(StringUtils.isNotBlank(url)){
            return url.split("\\?")[0];
        }
        return "";
    }


    public static void generateMerchantSignedUrl(MerchantInfoEntity info){
        info.setIdCardFrontImageUrl(generateSignedUrl(info.getIdCardFrontImageUrl(),AliOss.BUCKETNAME_PRIVATE));
        info.setIdCardBackImageUrl(generateSignedUrl(info.getIdCardBackImageUrl(),AliOss.BUCKETNAME_PRIVATE));
    }
}
