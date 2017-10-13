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
@Table(name = "tubobo_merchant_message_settings")
@DynamicInsert
@DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantMessageSettingsEntity extends BaseMerchantEntity {
    private String userId;
    private Boolean messageOpen;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getMessageOpen() {
        return messageOpen;
    }

    public void setMessageOpen(Boolean messageOpen) {
        this.messageOpen = messageOpen;
    }

    @Override
    public String toString() {
        return "MerchantSettingsEntity{" +
                "userId='" + userId + '\'' +
                ", messageOpen=" + messageOpen +
                '}';
    }
}
