package com.xinguang.tubobo.impl.wechat.exception;

public class WechatException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String errCode;
    private String errMsg;

    public WechatException() {
        super();
    }

    public WechatException(String errCode, String errMsg) {
        super(errCode + ":" + errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

}