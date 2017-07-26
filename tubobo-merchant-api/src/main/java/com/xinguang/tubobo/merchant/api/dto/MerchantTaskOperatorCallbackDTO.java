package com.xinguang.tubobo.merchant.api.dto;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/30.
 */
public class MerchantTaskOperatorCallbackDTO implements Serializable {
    private String taskNo;
    private Date operateTime;

    public MerchantTaskOperatorCallbackDTO(){}

    public MerchantTaskOperatorCallbackDTO(String taskNo, Date operateTime){
        this.taskNo = taskNo;
        this.operateTime = operateTime;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return "MerchantTaskOperatorCallbackDTO{" +
                "taskNo='" + taskNo + '\'' +
                ", operateTime=" + operateTime +
                '}';
    }
}
