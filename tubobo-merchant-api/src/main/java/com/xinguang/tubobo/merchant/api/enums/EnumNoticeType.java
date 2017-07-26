package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by xuqinghua on 2017/7/14.
 */
public enum EnumNoticeType {
    SYSTEM("系统公告", "SYSTEM"),
    ORDER("订单状态", "ORDER"),
    AUDIT("审核状态", "AUDIT");

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
