package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by Administrator on 2017/5/16.
 */
public enum  EnumInentifyType {
    PERSON("个人", "PERSON"),
    MERCHANT("商家", "MERCHANT");

    private String name;
    private String value;

    EnumInentifyType(String name, String value){
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
