package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * 地址信息.
 */
public class AddressInfoDTO implements Serializable{
    private String addressCity;//地址缩略
    private String addressDetail;//Y
    private String addressDistrict;//地址名称
    private String addressProvince;
    private Double latitude;//Y
    private Double longitude;//Y
    private String name;
    private String telephone;  //Y

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
        return "AddressInfoDTO{" +
                "addressCity='" + addressCity + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                ", addressDistrict='" + addressDistrict + '\'' +
                ", addressProvince='" + addressProvince + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
