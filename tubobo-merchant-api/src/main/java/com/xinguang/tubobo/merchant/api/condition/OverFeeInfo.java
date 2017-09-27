package com.xinguang.tubobo.merchant.api.condition;

import java.io.Serializable;

/**
 * Created by yangxb on 2017/9/11.
 */
public class OverFeeInfo implements Serializable {
    private Double peekOverFee;//	高峰溢价		　
    private Double totalOverFee;//	总溢价		　
    private Double weatherOverFee;//	天气溢价		　

    public Double getPeekOverFee() {
        return peekOverFee;
    }

    public void setPeekOverFee(Double peekOverFee) {
        this.peekOverFee = peekOverFee;
    }

    public Double getTotalOverFee() {
        return totalOverFee;
    }

    public void setTotalOverFee(Double totalOverFee) {
        this.totalOverFee = totalOverFee;
    }

    public Double getWeatherOverFee() {
        return weatherOverFee;
    }

    public void setWeatherOverFee(Double weatherOverFee) {
        this.weatherOverFee = weatherOverFee;
    }
}
