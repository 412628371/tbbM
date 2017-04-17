/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.api.rider.dto;

import java.io.Serializable;
import java.util.Date;

public class RiderInfoDTO implements Serializable{

	private String userId;

	private String riderStatus;
	private String phone;
	private String realName;
	private String nickName;
	private String idCardNo;
	private String headImage;
	private String idCardImageFront;
	private String idCardImageBack;
	private Date applyDate;
	private Date verifyDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRiderStatus() {
		return riderStatus;
	}

	public void setRiderStatus(String riderStatus) {
		this.riderStatus = riderStatus;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getHeadImage() {
		return headImage;
	}

	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}

	public String getIdCardImageFront() {
		return idCardImageFront;
	}

	public void setIdCardImageFront(String idCardImageFront) {
		this.idCardImageFront = idCardImageFront;
	}

	public String getIdCardImageBack() {
		return idCardImageBack;
	}

	public void setIdCardImageBack(String idCardImageBack) {
		this.idCardImageBack = idCardImageBack;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

}