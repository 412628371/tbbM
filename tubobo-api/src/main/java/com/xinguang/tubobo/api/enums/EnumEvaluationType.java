package com.xinguang.tubobo.api.enums;

public enum EnumEvaluationType{

	RECEIVE("收件评价", "RECEIVE"),
	SEND("寄件评价", "SEND");
	
    private String name;
    private String value;
     
    private EnumEvaluationType(String name,String value){
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
