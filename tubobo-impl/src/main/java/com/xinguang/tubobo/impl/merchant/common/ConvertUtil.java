package com.xinguang.tubobo.impl.merchant.common;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ConvertUtil {
    public static Long convertYuanToFen(Double yuan){
        if (yuan == null || yuan < 0)
            return 0L;
        return Math.round(yuan*100);
    }

    public static String handleNullString(String nullStr){
        if (null == nullStr)
            return "";
        return nullStr;
    }
}
