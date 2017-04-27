package com.xinguang.tubobo.merchant.web.request;

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

    private String addressProvince;//详细地址
    private String addressCity;//地址缩略
    private String addressDistrict;//地址名称
    @Size(min = 1,max = 100,message = "长度为1-100")
    private String addressDetail;//地址名称
    private String addressStreet;//地址名称
    @Size(min = 1,max = 100,message = "长度为1-100")
    private String addressRoomNo;//门牌号

    @NotBlank(message = "身份证正面图片不能为空")
    private String idCardBackImageUrl;
    @NotBlank(message = "身份证反面图片不能为空")
    private String idCardFrontImageUrl;

    @Range(min=0,max=180,message="纬度必须在0度至180度之间")
    private double latitude;
    @Range(min=0,max=180,message="经度必须在0度至180度之间")
    private double longitude;

    @NotBlank(message = "不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_ID_CARD ,message = "格式错误")
    private String idCardNo;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 20,message = "长度为1-20")
    private String realName;

    @NotBlank(message = "不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_PHONE,message = "手机号格式错误")
    private String phone;
    @NotBlank(message = "店铺名称不能为空")
    @Size(min = 1, max = 50,message = "长度为1-50")
    private String merchantName;

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

    @Override
    public String toString() {
        return "ShopIdentifyRequest{" +
                "addressProvince='" + addressProvince + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressDistrict='" + addressDistrict + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", idCardBackImageUrl='" + idCardBackImageUrl + '\'' +
                ", idCardFrontImageUrl='" + idCardFrontImageUrl + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", idCardNo='" + idCardNo + '\'' +
                ", realName='" + realName + '\'' +
                ", phone='" + phone + '\'' +
                ", merchantName='" + merchantName + '\'' +
                '}';
    }
}
