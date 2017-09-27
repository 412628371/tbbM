/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MerchantUnsettledDTO implements Serializable{

	private String orderNo;//订单No
	private String unsettledStatus;         //驿站订单 未妥投状态  默认""  0:未妥投处理中 1：未妥投已处理
	private String unsettledReason;         //驿站订单 未妥投原因
	private Date  unsettledTime;       //商家确认未妥投时间
	private Date  deliveryTime;        //骑手提交未妥投申请时间
    private  Double expiredMinute;      //超时分钟数

    public Double getExpiredMinute() {
        return expiredMinute;
    }

    public void setExpiredMinute(Double expiredMinute) {
        this.expiredMinute = expiredMinute;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUnsettledStatus() {
        return unsettledStatus;
    }

    public void setUnsettledStatus(String unsettledStatus) {
        this.unsettledStatus = unsettledStatus;
    }

    public String getUnsettledReason() {
        return unsettledReason;
    }

    public void setUnsettledReason(String unsettledReason) {
        this.unsettledReason = unsettledReason;
    }

    public Date getUnsettledTime() {
        return unsettledTime;
    }

    public void setUnsettledTime(Date unsettledTime) {
        this.unsettledTime = unsettledTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}