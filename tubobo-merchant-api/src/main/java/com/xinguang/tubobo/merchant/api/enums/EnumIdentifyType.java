package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by Administrator on 2017/5/16.
 */
public enum EnumIdentifyType {
    CONSIGNOR("货主", "CONSIGNOR"),
    MERCHANT("商家", "MERCHANT");

    private String name;
    private String value;

    EnumIdentifyType(String name, String value){
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
