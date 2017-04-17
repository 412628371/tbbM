package com.xinguang.tubobo.impl.wechat.enums;

public enum EnumEventType{
	
	TEMPLATESENDJOBFINISH("模版消息发送完成","TEMPLATESENDJOBFINISH"),
	CLICK("点击自定义菜单","CLICK"),
	VIEW("点击菜单跳转链接","VIEW"),
	SUBSCRIBE("订阅","subscribe"),
	UNSUBSCRIBE("取消订阅","unsubscribe"),
	SCAN("扫描","SCAN"),
	LOCATION("上报位置","LOCATION");
	
	private String label;
	private String value;
     
    private EnumEventType(String label, String value){
        this.label = label;
        this.value = value;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
     
}
