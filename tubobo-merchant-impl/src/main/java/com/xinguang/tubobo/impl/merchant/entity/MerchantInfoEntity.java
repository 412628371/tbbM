/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.entity;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tubobo_merchant_info")
@DynamicInsert
@DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantInfoEntity extends BaseMerchantEntity {

	private static final long serialVersionUID = 1L;

	private String userId;
	private Long accountId;

	private String merchantStatus;
	private String merchantName;			//商家店铺名称
	private String realName;				//商家姓名
	private String phone;
	private String idCardNo;
	private String addressProvince;			//详细地址
	private String addressCity;				//地址缩略
	private String addressDistrict;			//地址名称
	private String addressDetail;			//地址详情
	private String addressStreet;			//地址名称
	private String avatarUrl;
	private String idCardFrontImageUrl;
	private String idCardBackImageUrl;
	private Double longitude;
	private Double latitude;
	private Date applyDate;
	private Date verifyDate;
	private String addressRoomNo;			//门牌号
	private String shopImageUrl;
	private String shopImageUrl2;			//第二张商店照片
	private String shopLicencesImgUrl;		//营业执照照片
	private Boolean hasSetPayPwd;
	private Boolean enablePwdFree;
	private String identifyType;
	private String consignorStatus;

	private String bdCode;					//bd邀请码
	private String addressAdCode;			//高德区域编码
	private String reason;              //申请失败原因
	private Date bdUpdateDate;			 //bd邀请码绑定时间

	private String hygieneLicense;		//卫生许可证
	private Date enterTime;			 //入住驿站时间
	private Long providerId;			 //服务商ID
	private String providerName;			 //服务商名称
	private String bindStatus;			 //绑定状态INIT 初始化，SUCCESS 同意授权（绑定成功，REJECT 拒绝授权（绑定失败），NOOPERATE 	未操作（未绑定），UNBUNDLE 已解绑
	private Date unbundleTime;			 //解绑时间

	private Long merTypeId;			//商家类型id

	public Long getMerTypeId() {
		return merTypeId;
	}

	public void setMerTypeId(Long merTypeId) {
		this.merTypeId = merTypeId;
	}

	public String getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(String bindStatus) {
		this.bindStatus = bindStatus;
	}

	public Date getUnbundleTime() {
		return unbundleTime;
	}

	public void setUnbundleTime(Date unbundleTime) {
		this.unbundleTime = unbundleTime;
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

	public String getHygieneLicense() {
		return hygieneLicense;
	}

	public void setHygieneLicense(String hygieneLicense) {
		this.hygieneLicense = hygieneLicense;
	}

	public Date getBdUpdateDate() {
		return bdUpdateDate;
	}

	public void setBdUpdateDate(Date bdUpdateDate) {
		this.bdUpdateDate = bdUpdateDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAddressAdCode() {
		return addressAdCode;
	}

	public void setAddressAdCode(String addressAdCode) {
		this.addressAdCode = addressAdCode;
	}

	public String getBdCode() {
		return bdCode;
	}

	public void setBdCode(String bdCode) {
		this.bdCode = bdCode;
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

	public String getShopImageUrl() {
		return shopImageUrl;
	}

	public void setShopImageUrl(String shopImageUrl) {
		this.shopImageUrl = shopImageUrl;
	}

	public String getAddressRoomNo() {
		return addressRoomNo;
	}

	public void setAddressRoomNo(String addressRoomNo) {
		this.addressRoomNo = addressRoomNo;
	}


	public Boolean getHasSetPayPwd() {
		return hasSetPayPwd;
	}

	public void setHasSetPayPwd(Boolean hasSetPayPwd) {
		this.hasSetPayPwd = hasSetPayPwd;
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

	public Boolean getEnablePwdFree() {
		return enablePwdFree;
	}

	public void setEnablePwdFree(Boolean enablePwdFree) {
		this.enablePwdFree = enablePwdFree;
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

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	@Override
	public String toString() {
		return "MerchantInfoEntity{" +
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
				", addressRoomNo='" + addressRoomNo + '\'' +
				", shopImageUrl='" + shopImageUrl + '\'' +
				", hasSetPayPwd=" + hasSetPayPwd +
				", enablePwdFree=" + enablePwdFree +
				", identifyType='" + identifyType + '\'' +
				", consignorStatus='" + consignorStatus + '\'' +
				'}';
	}
}