package com.xinguang.tubobo.impl.merchant.common;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MerchantConstants {
    public static final String DEFAULT_PAY_PASSWORD = "111111";
    public static final String TOKEN_HEADER = "Authorization";
    public static final Double DISPATCH_RADIUS_BY_KiloMILLS = 3.00;
    public static final int PAY_EXPIRED_TIME_BY_MilSECONDS = 1800000;
    public static final int GRAB_EXPIRED_TIME_BY_MilSECONDS = 300000;

    public static final String REQ_ORDER_CANCEL = "CANCEL";
    public static final String REQ_ORDER_DELETE = "DELETE";
    public static final String MERCHANT_RECHARGE_TITLE = "商家充值";
    public static final String PAY_REJECT_REMARKS_OVERTIME = "暂无人接单，订单已取消";
    public static final String PAY_REJECT_REMARKS_CANCEL = "订单已取消  ";

    public static final String  TITLE_RECHARGE = "充值";
    public static final String  TITLE_WITHDRAW= "提现";
    public static final String  TITLE_PAY = "付款";
    public static final String  TITLE_FINE = "罚款";
    public static final String  TITLE_RECEIVE= "收款";
}
