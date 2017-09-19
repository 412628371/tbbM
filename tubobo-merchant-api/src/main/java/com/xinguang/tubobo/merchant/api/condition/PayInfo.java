package com.xinguang.tubobo.merchant.api.condition;

import java.io.Serializable;

/**
 * Created by yangxb on 2017/9/11.
 */
public class PayInfo implements Serializable {
    private Double deliveryFee;//	配送费	number	　
    private Double payAmount;//	支付金额	number	　
    private String payStatus;//	支付状态	string	UNPAY-未支付；PAID-已支付
    private Double tipFee;//	小费	number	　

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Double getTipFee() {
        return tipFee;
    }

    public void setTipFee(Double tipFee) {
        this.tipFee = tipFee;
    }
}
