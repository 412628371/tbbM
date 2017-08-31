package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by lvhantai on 2017/8/31.
 */
public class MerchantOvertimeFeeConfigDTO implements Serializable{
    private Double initDistance;        //'预计送达起始距离
    private Double initMinute;          //'预计送达起始时间
    private Double perDistanceMinute;   //'每公里增加分钟
    private Double beginMinute;         //'阶梯定价开始分钟
    private Double endMinute;           //'阶梯定价结束分钟
    private Double riderFines;          //'骑手罚款
    private Double merchantCompensation;//'商家获赔

    public Double getInitDistance() {
        return initDistance;
    }

    public void setInitDistance(Double initDistance) {
        this.initDistance = initDistance;
    }

    public Double getInitMinute() {
        return initMinute;
    }

    public void setInitMinute(Double initMinute) {
        this.initMinute = initMinute;
    }

    public Double getPerDistanceMinute() {
        return perDistanceMinute;
    }

    public void setPerDistanceMinute(Double perDistanceMinute) {
        this.perDistanceMinute = perDistanceMinute;
    }

    public Double getBeginMinute() {
        return beginMinute;
    }

    public void setBeginMinute(Double beginMinute) {
        this.beginMinute = beginMinute;
    }

    public Double getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Double endMinute) {
        this.endMinute = endMinute;
    }

    public Double getRiderFines() {
        return riderFines;
    }

    public void setRiderFines(Double riderFines) {
        this.riderFines = riderFines;
    }

    public Double getMerchantCompensation() {
        return merchantCompensation;
    }

    public void setMerchantCompensation(Double merchantCompensation) {
        this.merchantCompensation = merchantCompensation;
    }
}
