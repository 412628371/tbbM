package com.xinguang.tubobo.merchant.api.enums;

public enum EnumMsgReceiverSetting {

	ACCEPT("接受", "0"),
	REFUSE("不接受", "1");

    private String name;
    private String value;

    private EnumMsgReceiverSetting(String name, String value){
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
