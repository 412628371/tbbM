package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by lvhantai on 2017/6/23.
 */
public enum EnumAppointType {
    DELIVERY_IMMED("立即配送","immediate"),
    DELIVERY_APPOINT("预约配送","appoint");

    private String name;
    private String value;

    EnumAppointType(String name, String value) {
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
