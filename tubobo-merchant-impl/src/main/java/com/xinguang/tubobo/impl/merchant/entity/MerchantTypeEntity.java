package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yangxb on 2017/10/13.
 */
@Entity
@Table(name = "tubobo_merchant_type")
@DynamicInsert
@DynamicUpdate
public class MerchantTypeEntity extends BaseMerchantEntityId {
    private String name; //名称
    private String describtion; //描述
    private Long temId;//对应模板id
    private String temName;//对应模板名称
    private Integer commissionRate;// 佣金比例

    public Integer getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Integer commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribtion() {
        return describtion;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public Long getTemId() {
        return temId;
    }

    public void setTemId(Long temId) {
        this.temId = temId;
    }

    public String getTemName() {
        return temName;
    }

    public void setTemName(String temName) {
        this.temName = temName;
    }
}
