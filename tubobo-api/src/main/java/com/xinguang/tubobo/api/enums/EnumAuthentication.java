package com.xinguang.tubobo.api.enums;

public enum EnumAuthentication {

    INIT("未认证", "INIT"),
    APPLY("认证中", "APPLY"),
    SUCCESS("认证成功", "SUCCESS"),
    FAIL("认证失败", "FAIL"),
    FROZEN("冻结", "FROZEN")
    ;

    EnumAuthentication(String name, String value){
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
