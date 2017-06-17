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
    private String riderCarNo;
    private String riderCarType;

    private String taskNo;
    private Date grabTime;
    private Double pickupDistance;
    private Date expectFinishTime;

    public MerchantGrabCallbackDTO(String riderId, String riderName, String riderPhone,
                                   String taskNo, Date grabTime,Double pickupDistance,
                                   Date expectFinishTime){
        this.riderId = riderId;
        this.riderName = riderName;
        this.riderPhone = riderPhone;
        this.taskNo = taskNo;
        this.grabTime = grabTime;
        this.pickupDistance = pickupDistance;
        this.expectFinishTime = expectFinishTime;
    }
    public MerchantGrabCallbackDTO(String riderId, String riderName, String riderPhone,
                                   String taskNo, Date grabTime,Double pickupDistance,
                                   Date expectFinishTime,String riderCarNo,String riderCarType){
        this.riderId = riderId;
        this.riderName = riderName;
        this.riderPhone = riderPhone;
        this.taskNo = taskNo;
        this.grabTime = grabTime;
        this.pickupDistance = pickupDistance;
        this.expectFinishTime = expectFinishTime;
        this.riderCarNo = riderCarNo;
        this.riderCarType = riderCarType;
    }

    public MerchantGrabCallbackDTO(){

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

    public Double getPickupDistance() {
        return pickupDistance;
    }

    public void setPickupDistance(Double pickupDistance) {
        this.pickupDistance = pickupDistance;
    }

    public Date getExpectFinishTime() {
        return expectFinishTime;
    }

    public void setExpectFinishTime(Date expectFinishTime) {
        this.expectFinishTime = expectFinishTime;
    }

    @Override
    public String toString() {
        return "MerchantGrabCallbackDTO{" +
                "riderId='" + riderId + '\'' +
                ", riderName='" + riderName + '\'' +
                ", riderPhone='" + riderPhone + '\'' +
                ", riderCarNo='" + riderCarNo + '\'' +
                ", riderCarType='" + riderCarType + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", grabTime=" + grabTime +
                ", pickupDistance=" + pickupDistance +
                ", expectFinishTime=" + expectFinishTime +
                '}';
    }

    public String getRiderCarNo() {
        return riderCarNo;
    }

    public void setRiderCarNo(String riderCarNo) {
        this.riderCarNo = riderCarNo;
    }

    public String getRiderCarType() {
        return riderCarType;
    }

    public void setRiderCarType(String riderCarType) {
        this.riderCarType = riderCarType;
    }
}
