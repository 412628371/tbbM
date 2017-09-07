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
	private Long accountId;

	private String merchantStatus;
	private String merchantName;//商家店铺名称
	private String realName;//商家姓名
	private String phone;
	private String idCardNo;
	private String addressProvince;//详细地址
	private String addressCity;//地址缩略
	private String addressDistrict;//地址名称
	private String addressDetail;//地址详情
	private String addressStreet;//地址名称
	private String avatarUrl;
	private String idCardFrontImageUrl;
	private String idCardBackImageUrl;
	private double longitude;
	private double latitude;
	private Date applyDate;
	private Date verifyDate;

	private String shopImageUrl;
	private String shopImageUrl2;			//第二张商店照片
	private String shopLicencesImgUrl;		//营业执照照片
	private Boolean enablePwdFree;

	private String identifyType;
	private String consignorStatus;

	private String bdCode;					//bd邀请码字段
	private Date bdUpdateDate;				//bd邀请码注册时间


	private Date createDate;
	private Date updateDate;

	private String cityCode;
	private String hygieneLicense;		//卫生许可证

	private Date enterTime;			 //入住驿站时间
	private Long providerId;			 //服务商ID
	private String providerName;			 //服务商名称

	public String getHygieneLicense() {
		return hygieneLicense;
	}

	public void setHygieneLicense(String hygieneLicense) {
		this.hygieneLicense = hygieneLicense;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Date getBdUpdateDate() {
		return bdUpdateDate;
	}

	public void setBdUpdateDate(Date bdUpdateDate) {
		this.bdUpdateDate = bdUpdateDate;
	}

	public String getBdCode() {
		return bdCode;
	}

	public void setBdCode(String bdCode) {
		this.bdCode = bdCode;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public String getAddressProvince() {
		return addressProvince;
	}

	public void setAddressProvince(String addressProvince) {
		this.addressProvince = addressProvince;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressDistrict() {
		return addressDistrict;
	}

	public void setAddressDistrict(String addressDistrict) {
		this.addressDistrict = addressDistrict;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public String getAddressStreet() {
		return addressStreet;
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getIdCardFrontImageUrl() {
		return idCardFrontImageUrl;
	}

	public void setIdCardFrontImageUrl(String idCardFrontImageUrl) {
		this.idCardFrontImageUrl = idCardFrontImageUrl;
	}

	public String getIdCardBackImageUrl() {
		return idCardBackImageUrl;
	}

	public void setIdCardBackImageUrl(String idCardBackImageUrl) {
		this.idCardBackImageUrl = idCardBackImageUrl;
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

	public String getShopImageUrl() {
		return shopImageUrl;
	}

	public void setShopImageUrl(String shopImageUrl) {
		this.shopImageUrl = shopImageUrl;
	}

	public Boolean getEnablePwdFree() {
		return enablePwdFree;
	}

	public void setEnablePwdFree(Boolean enablePwdFree) {
		this.enablePwdFree = enablePwdFree;
	}

	public String getIdentifyType() {
		return identifyType;
	}

	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}

	public String getConsignorStatus() {
		return consignorStatus;
	}

	public void setConsignorStatus(String consignorStatus) {
		this.consignorStatus = consignorStatus;
	}

	public String getShopImageUrl2() {
		return shopImageUrl2;
	}

	public void setShopImageUrl2(String shopImageUrl2) {
		this.shopImageUrl2 = shopImageUrl2;
	}

	public String getShopLicencesImgUrl() {
		return shopLicencesImgUrl;
	}

	public void setShopLicencesImgUrl(String shopLicencesImgUrl) {
		this.shopLicencesImgUrl = shopLicencesImgUrl;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@Override
	public String toString() {
		return "MerchantInfoDTO{" +
				"userId='" + userId + '\'' +
				", accountId=" + accountId +
				", merchantStatus='" + merchantStatus + '\'' +
				", merchantName='" + merchantName + '\'' +
				", realName='" + realName + '\'' +
				", phone='" + phone + '\'' +
				", idCardNo='" + idCardNo + '\'' +
				", addressProvince='" + addressProvince + '\'' +
				", addressCity='" + addressCity + '\'' +
				", addressDistrict='" + addressDistrict + '\'' +
				", addressDetail='" + addressDetail + '\'' +
				", addressStreet='" + addressStreet + '\'' +
				", avatarUrl='" + avatarUrl + '\'' +
				", idCardFrontImageUrl='" + idCardFrontImageUrl + '\'' +
				", idCardBackImageUrl='" + idCardBackImageUrl + '\'' +
				", longitude=" + longitude +
				", latitude=" + latitude +
				", applyDate=" + applyDate +
				", verifyDate=" + verifyDate +
				", shopImageUrl='" + shopImageUrl + '\'' +
				", enablePwdFree=" + enablePwdFree +
				", identifyType='" + identifyType + '\'' +
				", consignorStatus='" + consignorStatus + '\'' +
				'}';
	}
}