package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2017/4/13.
 */
@Entity
@Table(name = "tubobo_merchant_autoresend_settings")
@DynamicInsert
@DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AutoResendPostSettingEntity extends BaseMerchantEntity {
    private String userId;
    private Boolean autoPostOrderResendOpen;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getAutoPostOrderResendOpen() {
        return autoPostOrderResendOpen;
    }

    public void setAutoPostOrderResendOpen(Boolean autoPostOrderResendOpen) {
        this.autoPostOrderResendOpen = autoPostOrderResendOpen;
    }

    @Override
    public String toString() {
        return "AutoResendPostSettingEntity{" +
                "userId='" + userId + '\'' +
                ", autoPostOrderResendOpen=" + autoPostOrderResendOpen +
                '}';
    }
}
