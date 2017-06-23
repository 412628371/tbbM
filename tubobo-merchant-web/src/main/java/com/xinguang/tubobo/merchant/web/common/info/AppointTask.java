package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * Created by lvhantai on 2017/6/21.
 * 用车时间信息
 */
public class AppointTask implements Serializable{
    private String appointTime;
    private String deliveryType;

    public AppointTask(String appointTime, String deliveryType) {
        this.appointTime = appointTime;
        this.deliveryType = deliveryType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }
}
