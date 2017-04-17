package com.xinguang.tubobo.impl.tuboboZhushouVo;

/**
 * 问问助手app 快递员登录tokenId
 */

public class ResTokenId {
	
	private String tokenId;
//	private boolean ignorePhoneFlag;// 签收动作时候 是否忽略检验手机号  false:不忽略(默认)  true:忽略
	private String name;

	public ResTokenId(String tokenId){
		this.tokenId = tokenId;
//		this.ignorePhoneFlag = false;
	}

	public ResTokenId(String tokenId, String name){
		this.tokenId = tokenId;
//		this.ignorePhoneFlag = ignorePhoneFlag;
		this.name = name;
	}

	public String getTokenId() {
		return tokenId;
	}

//	public boolean isIgnorePhoneFlag() {
//		return ignorePhoneFlag;
//	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

//	public void setIgnorePhoneFlag(boolean ignorePhoneFlag) {
//		this.ignorePhoneFlag = ignorePhoneFlag;
//	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
