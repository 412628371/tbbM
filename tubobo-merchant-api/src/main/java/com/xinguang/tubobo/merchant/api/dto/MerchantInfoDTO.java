/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MerchantInfoDTO implements Serializable{

	private String userId;

	private String merchantStatus;
	private String merchantName;//商家店铺名称
	private String name;//商家姓名
	private String phone;
	private String idCardNo;
	private String standardAddress;//省市区地址
	private String detailAddress;//详细地址
	private String headImage;
	private String idCardImageFront;
	private String idCardImageBack;
	private double longitude;
	private double latitude;
	private Date applyDate;
	private Date verifyDate;

	private boolean pushMsgOrderExpired;
	private boolean pushMsgOrderGrabed;
	private boolean pushMsgOrderFinished;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMerchantStatus() {
		return merchantStatus;
	}

	public void setMerchantStatus(String merchantStatus) {
		this.merchantStatus = merchantStatus;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getStandardAddress() {
		return standardAddress;
	}

	public void setStandardAddress(String standardAddress) {
		this.standardAddress = standardAddress;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
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

	public boolean isPushMsgOrderExpired() {
		return pushMsgOrderExpired;
	}

	public void setPushMsgOrderExpired(boolean pushMsgOrderExpired) {
		this.pushMsgOrderExpired = pushMsgOrderExpired;
	}

	public boolean isPushMsgOrderGrabed() {
		return pushMsgOrderGrabed;
	}

	public void setPushMsgOrderGrabed(boolean pushMsgOrderGrabed) {
		this.pushMsgOrderGrabed = pushMsgOrderGrabed;
	}

	public boolean isPushMsgOrderFinished() {
		return pushMsgOrderFinished;
	}

	public void setPushMsgOrderFinished(boolean pushMsgOrderFinished) {
		this.pushMsgOrderFinished = pushMsgOrderFinished;
	}
}