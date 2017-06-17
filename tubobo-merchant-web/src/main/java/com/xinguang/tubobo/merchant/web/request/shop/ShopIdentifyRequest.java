package com.xinguang.tubobo.merchant.web.request.shop;

import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * Created by Administrator on 2017/4/13.
 */
public class ShopIdentifyRequest implements Serializable {

    @Size(max = 100,message = "addressProvince长度为最大100")
    private String addressProvince;//详细地址
    @Size(max = 100,message = "addressCity长度为最大100")
    private String addressCity;//地址缩略
    @Size(max = 100,message = "addressDistrict长度为最大100")
    private String addressDistrict;//地址名称
//    @NotBlank(message = "详细地址不能为空")
    @Size(min = 1,max = 200,message = "addressDetail长度为1-200")
    private String addressDetail;//地址名称
    @Size(max = 200,message = "addressStreet长度为最大200")
    private String addressStreet;//地址名称
    @NotBlank(message = "门牌号不能为空")
    @Size(min = 1,max = 100,message = "门牌号长度过长")
    private String addressRoomNo;//门牌号

    @NotBlank(message = "身份证正面图片不能为空")
    @Size(max = 255,message = "idCardBackImageUrl长度过大")
    private String idCardBackImageUrl;
    @NotBlank(message = "身份证反面图片不能为空")
    @Size(max = 255,message = "idCardFrontImageUrl长度过大")
    private String idCardFrontImageUrl;

    @Range(min=0,max=180,message="纬度必须在0度至180度之间")
    private double latitude;
    @Range(min=0,max=180,message="经度必须在0度至180度之间")
    private double longitude;

    @NotBlank(message = "身份证号码不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_ID_CARD ,message = "身份证号码有误")
    private String idCardNo;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 20,message = "长度为1-20")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_PHONE,message = "手机号格式错误")
    private String phone;
    @NotBlank(message = "店铺名称不能为空")
    @Size(min = 1, max = 50,message = "长度为1-50")
    private String merchantName;

    @NotBlank(message = "请输入支付密码")
    private String payPassword;
    @Size(min = 1, max = 255,message = "店铺照片地址过长")
    @NotBlank(message = "请上传店铺照片")
    private String shopImageUrl;

    public String getAddressRoomNo() {
        return addressRoomNo;
    }

    public void setAddressRoomNo(String addressRoomNo) {
        this.addressRoomNo = addressRoomNo;
    }

    public ShopIdentifyRequest() {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }

    @Override
    public String toString() {
        return "ShopIdentifyRequest{" +
                "addressProvince='" + addressProvince + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressDistrict='" + addressDistrict + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressRoomNo='" + addressRoomNo + '\'' +
                ", idCardBackImageUrl='" + idCardBackImageUrl + '\'' +
                ", idCardFrontImageUrl='" + idCardFrontImageUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", idCardNo='" + idCardNo + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", payPassword='" + payPassword + '\'' +
                ", shopImageUrl='" + shopImageUrl + '\'' +
                '}';
    }
}
