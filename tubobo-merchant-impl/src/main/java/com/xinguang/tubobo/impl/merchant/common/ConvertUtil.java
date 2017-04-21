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

    public static String formatMoneyToString(Long money){
        double d = money/100.0;
        String result = String .format("%.2f",d);
        return result;
    }
    public static void main(String[] args) {
        Double test = 10000000.00;
        Integer testInteger = convertYuanToFen(test).intValue();
        System.out.println(testInteger);
        System.out.println(formatMoneyToString(100000L));
    }
}
