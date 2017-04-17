package com.xinguang.tubobo.api.enums;

/**
 * 配送类型
 * （只有自提和配送）
 */
public enum EnumDeliveryType{
	ZITI ("自提", "0"),
	PEISONG("配送", "1");
	
    private String name;
    private String value;
     
    private EnumDeliveryType(String name,String value){
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
