package com.xinguang.tubobo.merchant.api.enums;

/**
 * 订单通知的类型，如已取货，已接单，已送达等等.
 */
public enum EnumOrderNoticeType {

    GRAB_EXPIRED("超时未接单", "GRAB_EXPIRED"),
    ACCEPTED("已接单", "ACCEPTED"),
    PICKED("已取货", "PICKED"),
    ADMIN_CANCEL("后台取消", "ADMIN_CANCEL"),
    FINISH("完成", "FINISH");

    private String name;
    private String value;

    EnumOrderNoticeType(String name, String value){
        this.name = name;
        this.value = value;
    }

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
