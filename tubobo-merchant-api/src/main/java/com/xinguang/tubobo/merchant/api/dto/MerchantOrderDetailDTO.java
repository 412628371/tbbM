package com.xinguang.tubobo.merchant.api.dto;

import com.xinguang.tubobo.merchant.api.condition.*;

import java.io.Serializable;

/**
 * Created by yangxb on 2017/9/11.
 */
public class MerchantOrderDetailDTO implements Serializable {
    private AddressInfoDTO consignor;
    private RiderInfo riderInfo;
    private OrderInfo orderInfo;
    private PayInfo payInfo;
    private AddressInfoDTO receiver;
    private OverFeeInfo  overFeeInfo;

    public OverFeeInfo getOverFeeInfo() {
        return overFeeInfo;
    }

    public void setOverFeeInfo(OverFeeInfo overFeeInfo) {
        this.overFeeInfo = overFeeInfo;
    }

    public AddressInfoDTO getConsignor() {
        return consignor;
    }

    public void setConsignor(AddressInfoDTO consignor) {
        this.consignor = consignor;
    }

    public RiderInfo getRiderInfo() {
        return riderInfo;
    }

    public void setRiderInfo(RiderInfo riderInfo) {
        this.riderInfo = riderInfo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }

    public AddressInfoDTO getReceiver() {
        return receiver;
    }

    public void setReceiver(AddressInfoDTO receiver) {
        this.receiver = receiver;
    }
}
