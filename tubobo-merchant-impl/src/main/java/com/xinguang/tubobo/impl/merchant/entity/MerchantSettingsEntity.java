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
@Table(name = "tubobo_merchant_settings")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantSettingsEntity extends BaseMerchantEntity {
    private String userId;
    private Boolean pushMsgOrderExpired;
    private Boolean pushMsgOrderGrabed;
    private Boolean pushMsgOrderFinished;
    private String deviceToken;
    private Boolean pushMsgVoiceOpen;

    public Boolean getPushMsgVoiceOpen() {
        return pushMsgVoiceOpen;
    }

    public void setPushMsgVoiceOpen(Boolean pushMsgVoiceOpen) {
        this.pushMsgVoiceOpen = pushMsgVoiceOpen;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getPushMsgOrderExpired() {
        return pushMsgOrderExpired;
    }

    public void setPushMsgOrderExpired(Boolean pushMsgOrderExpired) {
        this.pushMsgOrderExpired = pushMsgOrderExpired;
    }

    public Boolean getPushMsgOrderGrabed() {
        return pushMsgOrderGrabed;
    }

    public void setPushMsgOrderGrabed(Boolean pushMsgOrderGrabed) {
        this.pushMsgOrderGrabed = pushMsgOrderGrabed;
    }

    public Boolean getPushMsgOrderFinished() {
        return pushMsgOrderFinished;
    }

    public void setPushMsgOrderFinished(Boolean pushMsgOrderFinished) {
        this.pushMsgOrderFinished = pushMsgOrderFinished;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
