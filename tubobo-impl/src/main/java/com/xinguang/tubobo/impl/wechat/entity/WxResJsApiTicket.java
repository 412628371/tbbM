package com.xinguang.tubobo.impl.wechat.entity;


public class WxResJsApiTicket extends WxResBase{
	
	private String ticket;
    private String expires_in;

	public WxResJsApiTicket() {
	}


	public String getTicket() {
		return ticket;
	}


	public void setTicket(String ticket) {
		this.ticket = ticket;
	}


	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

}