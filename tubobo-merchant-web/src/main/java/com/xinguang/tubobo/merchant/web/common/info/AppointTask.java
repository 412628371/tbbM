package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * Created by lvhantai on 2017/6/21.
 * 用车时间信息
 */
public class AppointTask implements Serializable{
    private String appointTime;
    private String appointType;

    public AppointTask(){}

    public AppointTask(String appointTime, String appointType) {
        this.appointTime = appointTime;
        this.appointType = appointType;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getAppointType() {
        return appointType;
    }

    public void setAppointType(String appointType) {
        this.appointType = appointType;
    }
}
