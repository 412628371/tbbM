package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yangxb on 2017/10/13.
 */
@Entity
@Table(name = "tubobo_merchant_deliver_fee_tem")
@DynamicInsert
@DynamicUpdate
public class MerchantDeliverFeeTemEntity extends BaseMerchantEntityId {
    private String name; //名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
