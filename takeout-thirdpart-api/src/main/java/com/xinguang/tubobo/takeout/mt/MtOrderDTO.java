package com.xinguang.tubobo.takeout.mt;

import java.io.Serializable;
import java.util.Date;

/**
 * 美团订单DTO.
 */
public class MtOrderDTO implements Serializable {
    private String originOrderId;
    private String originOrderViewId;
    private String receiverPhone;
    private String receiverName;
    private String receiverAddressDetail;
    private Double receiverLatitude;
    private Double receiverLongitude;
    private Date originCreateTime;

    public String getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(String originOrderId) {
        this.originOrderId = originOrderId;
    }

    public String getOriginOrderViewId() {
        return originOrderViewId;
    }

    public void setOriginOrderViewId(String originOrderViewId) {
        this.originOrderViewId = originOrderViewId;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddressDetail() {
        return receiverAddressDetail;
    }

    public void setReceiverAddressDetail(String receiverAddressDetail) {
        this.receiverAddressDetail = receiverAddressDetail;
    }

    public Double getReceiverLatitude() {
        return receiverLatitude;
    }

    public void setReceiverLatitude(Double receiverLatitude) {
        this.receiverLatitude = receiverLatitude;
    }

    public Double getReceiverLongitude() {
        return receiverLongitude;
    }

    public void setReceiverLongitude(Double receiverLongitude) {
        this.receiverLongitude = receiverLongitude;
    }

    public Date getOriginCreateTime() {
        return originCreateTime;
    }

    public void setOriginCreateTime(Date originCreateTime) {
        this.originCreateTime = originCreateTime;
    }
}
