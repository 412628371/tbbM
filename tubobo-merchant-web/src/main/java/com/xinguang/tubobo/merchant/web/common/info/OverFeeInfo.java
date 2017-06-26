package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * Created by yanxu on 2017/6/26.
 * 溢价信息
 */
public class OverFeeInfo implements Serializable{
    private Double peekOverFee;
    private Double weatherOverFee;
    private Double totalOverFee;

    public Double getPeekOverFee() {
        return peekOverFee;
    }

    public void setPeekOverFee(Double peekOverFee) {
        this.peekOverFee = peekOverFee;
    }

    public Double getWeatherOverFee() {
        return weatherOverFee;
    }

    public void setWeatherOverFee(Double weatherOverFee) {
        this.weatherOverFee = weatherOverFee;
    }

    public Double getTotalOverFee() {
        return totalOverFee;
    }

    public void setTotalOverFee(Double totalOverFee) {
        this.totalOverFee = totalOverFee;
    }
}
