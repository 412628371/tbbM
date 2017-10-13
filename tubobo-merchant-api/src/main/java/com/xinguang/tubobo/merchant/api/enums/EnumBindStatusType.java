package com.xinguang.tubobo.merchant.api.enums;

/**
 * Created by xuqinghua on 2017/7/14.
 */
public enum EnumBindStatusType {
    INIT("初始化", "INIT"),
    SUCCESS("绑定成功", "SUCCESS"),
    REJECT("绑定失败", "REJECT"),
    NOOPERATE("绑定中", "NOOPERATE"),
    UNBUNDLE("已解绑","UNBUNDLE");

//    INIT    	初始化
//    SUCCESS 	同意授权（绑定成功）
//    REJECT  	拒绝授权（绑定失败）
//    NOOPERATE 	未操作（未绑定）
//    UNBUNDLE	已解绑

    EnumBindStatusType(String name, String value){
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
