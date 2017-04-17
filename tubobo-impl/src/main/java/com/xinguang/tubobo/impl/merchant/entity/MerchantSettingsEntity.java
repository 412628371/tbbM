package com.xinguang.tubobo.impl.merchant.entity;

import com.hzmux.hzcms.common.persistence.IdEntity;
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

    public MerchantSettingsEntity(String userId){
        this.userId = userId;
    }
    public boolean isPushMsgOrderExpired() {
        return pushMsgOrderExpired;
    }

    public void setPushMsgOrderExpired(boolean pushMsgOrderExpired) {
        this.pushMsgOrderExpired = pushMsgOrderExpired;
    }

    public boolean isPushMsgOrderGrabed() {
        return pushMsgOrderGrabed;
    }

    public void setPushMsgOrderGrabed(boolean pushMsgOrderGrabed) {
        this.pushMsgOrderGrabed = pushMsgOrderGrabed;
    }

    public boolean isPushMsgOrderFinished() {
        return pushMsgOrderFinished;
    }

    public void setPushMsgOrderFinished(boolean pushMsgOrderFinished) {
        this.pushMsgOrderFinished = pushMsgOrderFinished;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
