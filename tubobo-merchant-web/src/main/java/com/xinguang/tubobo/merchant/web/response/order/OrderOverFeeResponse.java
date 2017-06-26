package com.xinguang.tubobo.merchant.web.response.order;

/**
 * Created by Administrator on 2017/4/14.
 */
public class OrderOverFeeResponse {
    private Double peekOverFee;
    private Double totalOverFee;
    private Double weatherOverFee;

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
