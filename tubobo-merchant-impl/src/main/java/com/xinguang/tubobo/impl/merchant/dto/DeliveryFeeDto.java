package com.xinguang.tubobo.impl.merchant.dto;

/**
 * Created by yangxb on 2017/10/16.
 */
public class DeliveryFeeDto {
    private Double totalFee;
    private Double riderFee;
    private Double platformFee;

    public DeliveryFeeDto(Double totalFee, Double riderFee, Double platformFee) {
        this.totalFee = totalFee;
        this.riderFee = riderFee;
        this.platformFee = platformFee;
    }

    public DeliveryFeeDto() {
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getRiderFee() {
        return riderFee;
    }

    public void setRiderFee(Double riderFee) {
        this.riderFee = riderFee;
    }

    public Double getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(Double platformFee) {
        this.platformFee = platformFee;
    }

    @Override
    public String toString() {
        return "DeliveryFeeDto{" +
                "totalFee=" + totalFee +
                ", riderFee=" + riderFee +
                ", platformFee=" + platformFee +
                '}';
    }
}
