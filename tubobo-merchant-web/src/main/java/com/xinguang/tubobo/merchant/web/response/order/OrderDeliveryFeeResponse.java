package com.xinguang.tubobo.merchant.web.response.order;

/**
 * Created by Administrator on 2017/4/14.
 */
public class OrderDeliveryFeeResponse {
    private Double deliveryFee;

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @Override
    public String toString() {
        return "OrderDeliveryFeeResponse{" +
                "deliveryFee=" + deliveryFee +
                '}';
    }
}
