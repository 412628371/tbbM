package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by lvhantai on 2017/8/30.
 */
@Entity
@Table(name = "tubobo_merchant_compensate_fee_config")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantCompensateFeeConfigEntity extends BaseMerchantEntity{
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
