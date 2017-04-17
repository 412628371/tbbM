package com.xinguang.tubobo.api.rider.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务状态变化通知传输层实体.
 */
public class RiderTaskStatusNotifyDTO implements Serializable{

    private Integer taskStatus;//任务状态
    private Date pickTime;//取货时间
    private Date acceptTime;//接单时间
    private Date deliveryTime;//送达时间
    private String taskNo;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Date getPickTime() {
        return pickTime;
    }

    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }
}
