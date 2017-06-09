package com.xinguang.tubobo.merchant.web.request.order;

import org.hibernate.validator.constraints.Range;


/**
 * 计算配送费请求.
 */
public class OrderDeliveryFeeRequest {
    private String orderType;
    private String carType;

    @Range(min=0,max=180,message="经纬度必须在0度至180度之间")
    private Double receiverLatitude;
    @Range(min=0,max=180,message="经纬度必须在0度至180度之间")
    private Double receiverLongitude;

    @Range(min=0,max=180,message="经纬度必须在0度至180度之间")
    private Double senderLongitude;
    @Range(min=0,max=180,message="经纬度必须在0度至180度之间")
    private Double senderLatitude;

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Double getSenderLongitude() {
        return senderLongitude;
    }

    public void setSenderLongitude(Double senderLongitude) {
        this.senderLongitude = senderLongitude;
    }

    public Double getSenderLatitude() {
        return senderLatitude;
    }

    public void setSenderLatitude(Double senderLatitude) {
        this.senderLatitude = senderLatitude;
    }

    @Override
    public String toString() {
        return "OrderDeliveryFeeRequest{" +
                "orderType='" + orderType + '\'' +
                ", carType='" + carType + '\'' +
                ", receiverLatitude=" + receiverLatitude +
                ", receiverLongitude=" + receiverLongitude +
                ", senderLongitude=" + senderLongitude +
                ", senderLatitude=" + senderLatitude +
                '}';
    }

}
