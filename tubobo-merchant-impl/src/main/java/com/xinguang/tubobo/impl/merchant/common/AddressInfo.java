package com.xinguang.tubobo.impl.merchant.common;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 地址信息.
 */
public class AddressInfo implements Serializable{

    @Size(min = 1,max = 200,message = "addressDetail长度为1-200")
    private String addressDetail;
    @Size(max = 100,message = "addressProvince长度最大为100")
    private String addressProvince;
    @Size(max = 100,message = "addressCity长度最大为100")
    private String addressCity;//地址缩略
    @Size(max = 100,message = "addressDistrict长度最大为100")
    private String addressDistrict;//地址名称
    @Size(max = 200,message = "addressStreet长度最大为200")
    private String addressStreet;//地址名称
    @Size(min = 1,max = 200,message = "门牌号过长")
//    @NotBlank(message = "门牌号不能为空")
    private String addressRoomNo;//地址名称

    @Range(min=0,max=180,message="收货人位置纬度必须在0度至180度之间")
    private Double latitude;
    @Range(min=0,max=180,message="经度必须在0度至180度之间")
    private Double longitude;

    private String name;
//    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_PHONE,message = "手机号格式错误")
    private String telephone;

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
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

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getAddressRoomNo() {
        return addressRoomNo;
    }

    public void setAddressRoomNo(String addressRoomNo) {
        this.addressRoomNo = addressRoomNo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "addressDetail='" + addressDetail + '\'' +
                ", addressProvince='" + addressProvince + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", addressDistrict='" + addressDistrict + '\'' +
                ", addressStreet='" + addressStreet + '\'' +
                ", addressRoomNo='" + addressRoomNo + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
