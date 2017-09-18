package com.xinguang.tubobo.merchant.api.enums;

/**
 * 订单类型枚举类，区分大件订单（货主）和小件订单（商家）.
 */
public enum  EnumOrderType {
    SMALLORDER("小件订单", "smallOrder"),
    POSTORDER("驿站订单", "postOrder"),
    BIGORDER("大件订单", "bigOrder");

    EnumOrderType(String name, String value){
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
