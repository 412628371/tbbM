package com.xinguang.tubobo.merchant.api.dto;

import com.xinguang.tubobo.merchant.api.condition.OverFeeInfo;

import java.io.Serializable;

/**
 * Created by yangxb on 2017/9/11.
 */
public class MerchantOrderCreateResultDto implements Serializable {
    private String orderNo;//订单号
    private Double deliverDistance;//配送距离
    private Double deliveryFee;//配送费	　
    private OverFeeInfo overFeeInfo;//溢价信息

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getDeliverDistance() {
        return deliverDistance;
    }

    public void setDeliverDistance(Double deliverDistance) {
        this.deliverDistance = deliverDistance;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public OverFeeInfo getOverFeeInfo() {
        return overFeeInfo;
    }

    public void setOverFeeInfo(OverFeeInfo overFeeInfo) {
        this.overFeeInfo = overFeeInfo;
    }
}
