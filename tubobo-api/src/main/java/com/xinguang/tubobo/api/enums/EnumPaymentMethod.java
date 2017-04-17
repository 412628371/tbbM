package com.xinguang.tubobo.api.enums;

public enum  EnumPaymentMethod {
    ONLINE("线上", "ONLINE"),
    OFFLINE("线下", "OFFLINE");

    EnumPaymentMethod(String name,String value){
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
