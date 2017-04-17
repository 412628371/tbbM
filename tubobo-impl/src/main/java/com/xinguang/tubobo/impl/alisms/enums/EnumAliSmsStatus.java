package com.xinguang.tubobo.impl.alisms.enums;

public enum EnumAliSmsStatus{

	INIT("待发送", "0"),
	WAIT("等待回执", "1"),
	FAIL("发送失败", "2"),
	SUC("发送成功", "3"),
	EXCEPTION("异常状态", "10");
	
    private String name;
    private String value;
     
    private EnumAliSmsStatus(String name,String value){
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
