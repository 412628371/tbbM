package com.xinguang.tubobo.merchant.api.enums;

public enum EnumPayStatus {
    UNPAY("未支付", "UNPAY"),
    PAID("已支付", "PAID");

    EnumPayStatus(String name, String value){
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
