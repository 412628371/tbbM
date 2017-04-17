package com.xinguang.tubobo.api.enums;

public enum EnumAddressType{

	S("寄件地址", "S"),
	R("收件地址", "R");
	
    private String name;
    private String value;
     
    private EnumAddressType(String name,String value){
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
