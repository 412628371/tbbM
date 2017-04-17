package com.xinguang.tubobo.api.enums;

public enum EnumPayMethod {
    ALIPAY("阿里支付", "aliPay"),
    WXPAY("微信支付", "wxPay");

    EnumPayMethod(String name, String value){
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
