package com.xinguang.tubobo.merchant.api.util;

import com.baidu.disconf.client.usertools.DisconfDataGetter;

import java.util.Map;

/**
 * Created by lvhantai on 2017/6/14.
 */
public class ErrorMsgHelper {
    private static Map<String, Object> errorMap;

    public static void loadAll(){
        errorMap = DisconfDataGetter.getByFile("errormsg.properties");
    }

    public static String findByCode(String code) {
        if (null == errorMap)
            loadAll();
        return (String) errorMap.get(code);
    }
}