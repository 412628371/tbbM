package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/30.
 */
public class MerchantGrabCallbackDTO implements Serializable {
    private String riderId;
    private String riderName;
    private String riderPhone;
    private String taskNo;
    private Date grabTime;

    public MerchantGrabCallbackDTO(String riderId, String riderName, String riderPhone, String taskNo, Date grabTime){
        this.riderId = riderId;
        this.riderName = riderName;
        this.riderPhone = riderPhone;
        this.taskNo = taskNo;
        this.grabTime = grabTime;
    }
    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Date getGrabTime() {
        return grabTime;
    }

    public void setGrabTime(Date grabTime) {
        this.grabTime = grabTime;
    }

    @Override
    public String toString() {
        return "MerchantGrabCallbackDTO{" +
                "riderId='" + riderId + '\'' +
                ", riderName='" + riderName + '\'' +
                ", riderPhone='" + riderPhone + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", grabTime=" + grabTime +
                '}';
    }
}
