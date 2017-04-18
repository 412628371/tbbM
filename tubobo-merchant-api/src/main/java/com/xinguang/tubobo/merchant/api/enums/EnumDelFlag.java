package com.xinguang.tubobo.merchant.api.enums;

public enum EnumDelFlag{

	NORMAL("有效", "0"),
	DELETE("删除", "1");
	
    private String name;
    private String value;
     
    private EnumDelFlag(String name,String value){
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
