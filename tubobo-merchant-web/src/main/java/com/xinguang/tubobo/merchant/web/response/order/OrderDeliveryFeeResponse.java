package com.xinguang.tubobo.merchant.web.response.order;

import com.xinguang.tubobo.impl.merchant.dto.DeliveryFeeDto;

/**
 * Created by Administrator on 2017/4/14.
 */
public class OrderDeliveryFeeResponse {

    private DeliveryFeeDto deliveryFeeDto;
    private Double deliveryDistance;

    public Double getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(Double deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public DeliveryFeeDto getDeliveryFeeDto() {
        return deliveryFeeDto;
    }

    public void setDeliveryFeeDto(DeliveryFeeDto deliveryFeeDto) {
        this.deliveryFeeDto = deliveryFeeDto;
    }
}
