package com.xinguang.tubobo.merchant.web.request;

import org.hibernate.validator.constraints.Range;


/**
 * Created by Administrator on 2017/4/14.
 */
public class OrderDeliveryFeeRequest {
    private String goodsType;
    @Range(min=0,max=180,message="经纬度必须在0度至180度之间")
    private Double receiverLatitude;
    @Range(min=0,max=180,message="经纬度必须在0度至180度之间")
    private Double receiverLongitude;

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [goodsType]: ").append(goodsType)
                .append(", [receiverLatitude]").append(receiverLatitude)
                .append(", [receiverLongitude]: ").append(receiverLongitude);
        return sb.toString();
    }

    public static void main(String[] args) {
        OrderDeliveryFeeRequest request= new OrderDeliveryFeeRequest();
        request.setReceiverLatitude(12345.0);
        System.out.print(request.toString());
    }
}
