package com.xinguang.tubobo.merchant.web.request.shop.v2;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 完善店铺信息请求.
 */
public class ReqShopComplete implements Serializable {
    @Size(max = 100,message = "addressProvince长度为最大100")
    private String addressProvince;//详细地址
    @Size(max = 100,message = "addressCity长度为最大100")
    private String addressCity;//地址缩略
    @Size(max = 100,message = "addressDistrict长度为最大100")
    private String addressDistrict;//地址名称
    @Size(min = 1,max = 200,message = "addressDetail长度为1-200")
    private String addressDetail;//地址名称
    @Size(max = 200,message = "addressStreet长度为最大200")
    private String addressStreet;//地址名称
    @NotBlank(message = "门牌号不能为空")
    @Size(min = 1,max = 100,message = "门牌号长度过长")
    private String addressRoomNo;//门牌号

    @Range(min=0,max=180,message="纬度必须在0度至180度之间")
    private double latitude;
    @Range(min=0,max=180,message="经度必须在0度至180度之间")
    private double longitude;

    @NotBlank(message = "店铺名称不能为空")
    @Size(min = 1, max = 50,message = "长度为1-50")
    private String merchantName;

    @Size(min = 1, max = 255,message = "店铺照片地址过长")
//    @NotBlank(message = "请上传店铺照片")
    private String shopImageUrl;

    @Size(min = 1, max = 255,message = "店铺照片地址过长")
//    @NotBlank(message = "请上传店铺照片")
    private String shopImageUrl2;

    @Size(min = 1, max = 255,message = "营业执照照片地址过长")
//    @NotBlank(message = "请上传营业执照照片")
    private String shopLicencesImgUrl;

    //@Size(min = 1, max = 255,message = "卫生许可证照片地址过长")
    private String hygieneLicense;

    //@NotBlank(message = "区域编码不能为空")
    private String addressAdCode;//高德区域编码

    //@Size(min = 1, max = 50,message = "营业执照地址过长")
    private String shopLicencesNo;  //营业执照编号

    public String getShopLicencesNo() {
        return shopLicencesNo;
    }

    public void setShopLicencesNo(String shopLicencesNo) {
        this.shopLicencesNo = shopLicencesNo;
    }


    public String getHygieneLicense() {
        return hygieneLicense;
    }

    public void setHygieneLicense(String hygieneLicense) {
        this.hygieneLicense = hygieneLicense;
    }

    public String getAddressAdCode() {
        return addressAdCode;
    }

    public void setAddressAdCode(String addressAdCode) {
        this.addressAdCode = addressAdCode;
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

    public String getAddressRoomNo() {
        return addressRoomNo;
    }

    public void setAddressRoomNo(String addressRoomNo) {
        this.addressRoomNo = addressRoomNo;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getShopImageUrl() {
        return shopImageUrl;
    }

    public void setShopImageUrl(String shopImageUrl) {
        this.shopImageUrl = shopImageUrl;
    }
}
