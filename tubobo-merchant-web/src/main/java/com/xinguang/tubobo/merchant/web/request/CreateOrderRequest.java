package com.xinguang.tubobo.merchant.web.request;

import com.hzmux.hzcms.common.utils.ValidUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/13.
 */
public class CreateOrderRequest implements Serializable {
    @Size(min = 1,max = 200,message = "收货人地址详情不能为空")
    private String receiverAddressDetail;
    @Size(min = 1,max = 100,message = "省长度不符")
    private String receiverAddressProvince;
//    @Size(min = 1,max = 100,message = "市长度不符")
    private String receiverAddressCity;//地址缩略
//    @Size(max = 100,message = "区长度过大")
    private String receiverAddressDistrict;//地址名称
    private String receiverAddressStreet;//地址名称
    private String receiverGdPoiId;
//TODO
    @Range(min = 1,max = 100000,message = "配送费错误")
    private Double deliveryFee;
    private Double tipFee;

//    @NotBlank(message = "支付方式不能为空")
    private String payMethod;

    @Range(min=0,max=180,message="收货人位置纬度必须在0度至180度之间")
    private Double receiverLatitude;
    @Range(min=0,max=180,message="经度必须在0度至180度之间")
    private Double receiverLongitude;

//    @Length(min = 1,max = 100,message = "收货人姓名长度错误")
//    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;
    @NotBlank(message = "电话格式错误")
    private String receiverPhone;

    private String orderRemarks;

    public String getReceiverAddressDetail() {
        return receiverAddressDetail;
    }

    public void setReceiverAddressDetail(String receiverAddressDetail) {
        this.receiverAddressDetail = receiverAddressDetail;
    }

    public String getReceiverAddressProvince() {
        return receiverAddressProvince;
    }

    public void setReceiverAddressProvince(String receiverAddressProvince) {
        this.receiverAddressProvince = receiverAddressProvince;
    }

    public String getReceiverAddressCity() {
        return receiverAddressCity;
    }

    public void setReceiverAddressCity(String receiverAddressCity) {
        this.receiverAddressCity = receiverAddressCity;
    }

    public String getReceiverAddressDistrict() {
        return receiverAddressDistrict;
    }

    public void setReceiverAddressDistrict(String receiverAddressDistrict) {
        this.receiverAddressDistrict = receiverAddressDistrict;
    }

    public String getReceiverAddressStreet() {
        return receiverAddressStreet;
    }

    public void setReceiverAddressStreet(String receiverAddressStreet) {
        this.receiverAddressStreet = receiverAddressStreet;
    }

    public String getReceiverGdPoiId() {
        return receiverGdPoiId;
    }

    public void setReceiverGdPoiId(String receiverGdPoiId) {
        this.receiverGdPoiId = receiverGdPoiId;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getTipFee() {
        return tipFee;
    }

    public void setTipFee(Double tipFee) {
        this.tipFee = tipFee;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public double getReceiverLatitude() {
        return receiverLatitude;
    }

    public void setReceiverLatitude(double receiverLatitude) {
        this.receiverLatitude = receiverLatitude;
    }

    public double getReceiverLongitude() {
        return receiverLongitude;
    }

    public void setReceiverLongitude(double receiverLongitude) {
        this.receiverLongitude = receiverLongitude;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
                "receiverAddressDetail='" + receiverAddressDetail + '\'' +
                ", receiverAddressProvince='" + receiverAddressProvince + '\'' +
                ", receiverAddressCity='" + receiverAddressCity + '\'' +
                ", receiverAddressDistrict='" + receiverAddressDistrict + '\'' +
                ", receiverAddressStreet='" + receiverAddressStreet + '\'' +
                ", receiverGdPoiId='" + receiverGdPoiId + '\'' +
                ", deliveryFee=" + deliveryFee +
                ", tipFee=" + tipFee +
                ", payMethod='" + payMethod + '\'' +
                ", receiverLatitude=" + receiverLatitude +
                ", receiverLongitude=" + receiverLongitude +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", orderRemarks='" + orderRemarks + '\'' +
                '}';
    }
}
