package com.xinguang.tubobo.api.enums;

public enum EnumEmployeeType{

	KEEPER("门店店长", "10"),
	EMPLOYEE("直营快递员", "11"),
	PARTTIMEJOB("兼职快递员", "12");
	
    private String name;
    private String value;
     
    private EnumEmployeeType(String name,String value){
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
