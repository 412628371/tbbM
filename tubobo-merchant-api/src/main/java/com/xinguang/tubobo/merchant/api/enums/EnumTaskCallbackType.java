package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by xuqinghua on 2017/7/6.
 */
public enum  EnumTaskCallbackType {
    GRAB("已接单", "GRAB"),
    PICK("已取货", "PICK"),
    EXPIRED("过期未接单", "EXPIRED"),
    FINISH("送达", "FINISH");

    EnumTaskCallbackType(String name, String value){
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
