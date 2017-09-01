package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by lvhantai on 2017/8/31.
 */
public class MerchantCompensateFeeConfigDTO implements Serializable{
    private Double merDeductionFee;
    private Double merCompensationRiderFee;

    public Double getMerDeductionFee() {
        return merDeductionFee;
    }

    public void setMerDeductionFee(Double merDeductionFee) {
        this.merDeductionFee = merDeductionFee;
    }

    public Double getMerCompensationRiderFee() {
        return merCompensationRiderFee;
    }

    public void setMerCompensationRiderFee(Double merCompensationRiderFee) {
        this.merCompensationRiderFee = merCompensationRiderFee;
    }
}
