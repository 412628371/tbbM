package com.xinguang.tubobo.api.enums;

public enum EnumCustomerType {

	GENERAL("普通用户", "g"),
	BIGCLIENT("大客户", "b");
	
	private String name;
    private String value;
     
    private EnumCustomerType(String name,String value){
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
