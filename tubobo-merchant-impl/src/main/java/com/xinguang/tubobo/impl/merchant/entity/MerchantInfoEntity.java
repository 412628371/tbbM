/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.entity;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tubobo_merchant_info")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantInfoEntity extends BaseMerchantEntity {

	private static final long serialVersionUID = 1L;

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
	private Double longitude;
	private Double latitude;
	private Date applyDate;
	private Date verifyDate;
	private String addressRoomNo;//门牌号
	private String payPassword;

	public String getAddressRoomNo() {
		return addressRoomNo;
	}

	public void setAddressRoomNo(String addressRoomNo) {
		this.addressRoomNo = addressRoomNo;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
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
		return ConvertUtil.handleNullString(merchantStatus);
	}

	public void setMerchantStatus(String merchantStatus) {
		this.merchantStatus = merchantStatus;
	}

	public String getMerchantName() {
		return ConvertUtil.handleNullString(merchantName);
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getRealName() {

		return ConvertUtil.handleNullString(realName);
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhone() {
		return  ConvertUtil.handleNullString(phone);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdCardNo() {
		return ConvertUtil.handleNullString(idCardNo);
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getAddressProvince() {
		return ConvertUtil.handleNullString(addressProvince);
	}

	public void setAddressProvince(String addressProvince) {
		this.addressProvince = addressProvince;
	}

	public String getAddressCity() {
		return ConvertUtil.handleNullString(addressCity);
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getAddressDistrict() {
		return ConvertUtil.handleNullString(addressDistrict);
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
		return ConvertUtil.handleNullString(addressStreet);
	}

	public void setAddressStreet(String addressStreet) {
		this.addressStreet = addressStreet;
	}

	public String getAvatarUrl() {
		return ConvertUtil.handleNullString(avatarUrl);
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getIdCardFrontImageUrl() {
		return ConvertUtil.handleNullString(idCardFrontImageUrl);
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

	public Double getLongitude() {
		if (longitude == null){
			return 0.0;
		}
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLatitude() {
		if (latitude == null){
			return 0.0;
		}
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
}