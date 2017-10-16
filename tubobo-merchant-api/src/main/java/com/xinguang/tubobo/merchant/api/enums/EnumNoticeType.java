package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by xuqinghua on 2017/7/14.
 */
public enum EnumNoticeType {
    SYSTEM("系统公告", "SYSTEM"),
    MONEY("金额变动", "MONEY"),
    ORDER("订单状态", "ORDER"),
    AUDIT("审核状态", "AUDIT"),
    MONEYSHORT("资金不足自动支付订单","MONEYSHORT"),
    BIND("商家绑定","POSTBIND"),
    UNBIND("商家解绑","POSTUNBIND");

    EnumNoticeType(String name, String value){
        this.name = name;
        this.value = value;
    }
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
