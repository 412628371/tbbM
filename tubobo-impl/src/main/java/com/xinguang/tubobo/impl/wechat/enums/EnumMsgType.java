package com.xinguang.tubobo.impl.wechat.enums;

public enum EnumMsgType{
	
	EVENT("事件推送","event"),
	TEXT("文本消息","text"),
	IMAGE("图片消息","image"),
	VOICE("语音消息","voice"),
	VIDEO("视频消息","video"),
	LINK("链接消息","link"),
	SHORTVIDEO("小视频消息","shortvideo"),
	LOCATION("地理位置消息","location");
	
	private String label;
	private String value;
     
    private EnumMsgType(String label, String value){
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
