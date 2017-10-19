package com.xinguang.tubobo.merchant.web.response;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13.
 */
public class MerchantInfoResponse implements Serializable{
    private String userId;
    private String addressProvince;//省
    private String addressCity;//市
    private String addressDistrict;//区
    private String addressDetail;//详细地址
    private String addressStreet;//街道
    private String addressRoomNo;//门牌号

    private String idCardBackImageUrl;
    private String idCardFrontImageUrl;
    private String avatarUrl;

    private String merchantStatus;

    private Double latitude;
    private Double longitude;

    private String phone;

    private String idCardNo;
    private String realName;
    private String merchantName;

    private String shopImageUrl;
    private String shopImageUrl2;
    private String shopLicencesImgUrl;  //营业执照
    private String hygieneLicense;//卫生许可证

    private String identifyType;
    private Boolean enablePwdFree;
    private Double nonConfidentialPaymentLimit;
    private Boolean hasSetPayPwd;
    private String consignorStatus;

    private String bdCode;					//bd邀请码
    private String addressAdCode;			//高德区域编码
    private String reason;                  //店铺审核失败原因
    private Boolean messageOpen;            //短信开关

    private Long providerId;			 //服务商ID

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Boolean getMessageOpen() {
        return messageOpen;
    }

    public void setMessageOpen(Boolean messageOpen) {
        this.messageOpen = messageOpen;
    }

    public String getHygieneLicense() {
        return hygieneLicense;
    }

    public void setHygieneLicense(String hygieneLicense) {
        this.hygieneLicense = hygieneLicense;
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

    public String getConsignorStatus() {
        return consignorStatus;
    }

    public void setConsignorStatus(String consignorStatus) {
        this.consignorStatus = consignorStatus;
    }

    public Double getNonConfidentialPaymentLimit() {
        return nonConfidentialPaymentLimit;
    }

    public void setNonConfidentialPaymentLimit(Double nonConfidentialPaymentLimit) {
        this.nonConfidentialPaymentLimit = nonConfidentialPaymentLimit;
    }

    public Boolean getEnablePwdFree() {
        return enablePwdFree;
    }

    public void setEnablePwdFree(Boolean enablePwdFree) {
        this.enablePwdFree = enablePwdFree;
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

    public String getIdCardBackImageUrl() {
        return idCardBackImageUrl;
    }

    public void setIdCardBackImageUrl(String idCardBackImageUrl) {
        this.idCardBackImageUrl = idCardBackImageUrl;
    }

    public String getIdCardFrontImageUrl() {
        return idCardFrontImageUrl;
    }

    public void setIdCardFrontImageUrl(String idCardFrontImageUrl) {
        this.idCardFrontImageUrl = idCardFrontImageUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAddressRoomNo() {
        return addressRoomNo;
    }

    public void setAddressRoomNo(String addressRoomNo) {
        this.addressRoomNo = addressRoomNo;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public Boolean getHasSetPayPwd() {
        return hasSetPayPwd;
    }

    public void setHasSetPayPwd(Boolean hasSetPayPwd) {
        this.hasSetPayPwd = hasSetPayPwd;
    }

    @Override
    public String toString() {
        return "MerchantInfoResponse{" +
                "userId='" + userId + '\'' +
                ", addressProvince='" + addressProvince + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressDistrict='" + addressDistrict + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressRoomNo='" + addressRoomNo + '\'' +
                ", idCardBackImageUrl='" + idCardBackImageUrl + '\'' +
                ", idCardFrontImageUrl='" + idCardFrontImageUrl + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", merchantStatus='" + merchantStatus + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", phone='" + phone + '\'' +
                ", idCardNo='" + idCardNo + '\'' +
                ", realName='" + realName + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", shopImageUrl='" + shopImageUrl + '\'' +
                ", identifyType='" + identifyType + '\'' +
                ", enablePwdFree=" + enablePwdFree +
                ", nonConfidentialPaymentLimit=" + nonConfidentialPaymentLimit +
                ", hasSetPayPwd=" + hasSetPayPwd +
                ", consignorStatus='" + consignorStatus + '\'' +
                '}';
    }
}
