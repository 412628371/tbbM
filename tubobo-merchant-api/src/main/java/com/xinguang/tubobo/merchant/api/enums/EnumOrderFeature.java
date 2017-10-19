package com.xinguang.tubobo.merchant.api.enums;

/**
 * 订单特征值
 */
public enum EnumOrderFeature {
    RIDER_CANCEL_RESEND ("骑手取消重发单", "RIDER_CANCEL_RESEND");

    EnumOrderFeature(String name, String value){
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
