package com.xinguang.tubobo.impl.merchant.common;


import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MerchantConstants {
    public static final String PUSH_ORDER_TYPE_BIG= "orderDetail-big";
    public static final String PUSH_ORDER_TYPE_SMALL= "orderDetail-small";

    public static final String ORDER_LIST_QUERY_CONDITION_ALL= "all";
    public static final String ORDER_LIST_QUERY_CONDITION_UNHANDLE= "UNHANDLE";
    public static final String ORDER_LIST_QUERY_CONDITION_FINISH= "FINISH";




    public static final String DEFAULT_SEPARATOR = ",";

    public static final String KEY_PWD_WRONG_TIMES_PAY = "KEY_PAY_PWD_WRONG_TIMES";
    public static final String KEY_PWD_WRONG_TIMES_MODIFY = "KEY_PWD_WRONG_TIMES_MODIFY";
    public static final String KEY_PWD_WRONG_TIMES_FREE = "KEY_PWD_WRONG_TIMES_FREE";

    public static final String PLATFORM_ID = "MERCHANT";

    public static final String TARGET_TYPE_RIDER = "RIDER";
    public static final String KEY_RATE_DELIVERY_SCORE = "RATE_DELIVERY_SCORE";
    public static final String KEY_RATE_SERVICE_SCORE = "RATE_SERVICE_SCORE";

    //    public static final String DEFAULT_PAY_PASSWORD = "111111";
    public static final String TOKEN_HEADER = "Authorization";
    public static final Double DISPATCH_RADIUS_BY_KiloMILLS = 3.00;
    public static final int PAY_EXPIRED_TIME_BY_MilSECONDS = 1800000;
    public static final int GRAB_EXPIRED_TIME_BY_MilSECONDS = 300000;

    public static final String REQ_ORDER_CANCEL = "CANCEL";
    public static final String REQ_ORDER_DELETE = "DELETE";
    public static final String MERCHANT_RECHARGE_TITLE = "商家充值";
    public static final String PAY_REJECT_REMARKS_OVERTIME = "暂无人接单，订单已取消";
    public static final String PAY_REJECT_REMARKS_CANCEL = "订单已取消  ";



    public static final String  PATTERN_PHONE= "^1\\d{10}?$";
    public static final String  PATTERN_ID_CARD= "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";

    /**
     * 根据订单类型，获得相应的推送参数
     * @param orderType
     * @return
     */
    public static String getPushParamByOrderType(String orderType){
        if (EnumOrderType.BIGORDER.getValue().equals(orderType)){
            return PUSH_ORDER_TYPE_BIG;
        }else if (EnumOrderType.SMALLORDER.getValue().equals(orderType)){
            return PUSH_ORDER_TYPE_SMALL;
        }
        return "";
    }
    public static void main(String[] args) {
        System.out.println(Pattern.matches(PATTERN_ID_CARD,"412702199001145034"));
        System.out.println(Pattern.matches(PATTERN_ID_CARD,"54242219861005262X"));
        System.out.println(Pattern.matches(PATTERN_ID_CARD,"45032619840627183x"));
        System.out.println(Pattern.matches(PATTERN_ID_CARD,"110105197109235829"));
        System.out.println(Pattern.matches(PATTERN_ID_CARD,"666666199009235829"));
    }
}
