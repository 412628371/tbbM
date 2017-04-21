package com.xinguang.tubobo.merchant.web.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/19.
 */
public class RespSettingsQuery {
    private Boolean pushMsgOrderExpired;
    private Boolean pushMsgOrderFinished;
    private Boolean pushMsgOrderGrabed;

    public Boolean getPushMsgOrderExpired() {
        return pushMsgOrderExpired;
    }

    public void setPushMsgOrderExpired(Boolean pushMsgOrderExpired) {
        this.pushMsgOrderExpired = pushMsgOrderExpired;
    }

    public Boolean getPushMsgOrderFinished() {
        return pushMsgOrderFinished;
    }

    public void setPushMsgOrderFinished(Boolean pushMsgOrderFinished) {
        this.pushMsgOrderFinished = pushMsgOrderFinished;
    }

    public Boolean getPushMsgOrderGrabed() {
        return pushMsgOrderGrabed;
    }

    public void setPushMsgOrderGrabed(Boolean pushMsgOrderGrabed) {
        this.pushMsgOrderGrabed = pushMsgOrderGrabed;
    }

    @Override
    public String toString() {
        return "RespSettingsQuery{" +
                "pushMsgOrderExpired=" + pushMsgOrderExpired +
                ", pushMsgOrderFinished=" + pushMsgOrderFinished +
                ", pushMsgOrderGrabed=" + pushMsgOrderGrabed +
                '}';
    }
}
