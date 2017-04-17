package com.xinguang.tubobo.api.enums;

/**
 * 配送方式
 * （到点自取、配送到家、代拿）
 * 可添加
 */
public enum EnumDeliveryMethod{
	ZIQU ("到店自取", "0"),
	PEISONG("配送到家", "1"),
	DAINA ("代拿", "2");
	
    private String name;
    private String value;
     
    private EnumDeliveryMethod(String name,String value){
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
