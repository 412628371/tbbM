package com.xinguang.tubobo.impl.alisms.enums;

public enum EnumAliSmsTemplate{
	
	QUJIAN_DEFAULT("默认取件通知", "qujian_default"),
	QUJIAN("取件通知", "qujian"),
	JIJIAN("寄件通知", "jijian"),
	JIJIAN_BATCH("大客户寄件通知", "jijian_batch"),
	REGISTER("注册", "r"),
	FINDPASSWORD("找回密码", "f");
	
    private String name;
    private String value;
     
    private EnumAliSmsTemplate(String name,String value){
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
