package com.xinguang.tubobo.impl.wechat.entity;


public class WxResMessageSend extends WxResBase{
	
	private String msgid;

	public WxResMessageSend() {
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

}