package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/4/13.
 */
@Entity
@Table(name = "tubobo_merchant_message_record")
@DynamicInsert
@DynamicUpdate
public class MerchantMessageRecordEntity extends BaseMerchantEntity {

    private String orderNo;
    private Long recordMessageId;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getRecordMessageId() {
        return recordMessageId;
    }

    public void setRecordMessageId(Long recordMessageId) {
        this.recordMessageId = recordMessageId;
    }
}
