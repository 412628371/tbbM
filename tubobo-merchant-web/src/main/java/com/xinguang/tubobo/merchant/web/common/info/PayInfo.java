package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;
import java.util.Date;

/**
 * 支付信息.
 */
public class PayInfo implements Serializable {
    private Long payRemainMillSeconds;

    private Double payAmount;//支付总金额
    private Double deliveryFee;//配送费
    private Double tipFee;//小费
    private String payStatus;//支付状态
    private String payMethod;//支付方式
    private Date payTime;//付款时间
    private Double expiredCompensation;//超时赔付expiredCompensation
    private Double cancelCompensation; //订单取消赔付
    private Double cancelFine;         //订单取消罚款
    private String    messageAmout;   //短信费用

    public Double getExpiredCompensation() {
        return expiredCompensation;
    }

    public void setExpiredCompensation(Double expiredCompensation) {
        this.expiredCompensation = expiredCompensation;
    }

    public Double getCancelCompensation() {
        return cancelCompensation;
    }

    public void setCancelCompensation(Double cancelCompensation) {
        this.cancelCompensation = cancelCompensation;
    }

    public Double getCancelFine() {
        return cancelFine;
    }

    public void setCancelFine(Double cancelFine) {
        this.cancelFine = cancelFine;
    }

    public Long getPayRemainMillSeconds() {
        return payRemainMillSeconds;
    }

    public void setPayRemainMillSeconds(Long payRemainMillSeconds) {
        this.payRemainMillSeconds = payRemainMillSeconds;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
