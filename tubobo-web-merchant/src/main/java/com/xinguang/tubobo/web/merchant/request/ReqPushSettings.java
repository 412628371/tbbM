package com.xinguang.tubobo.web.merchant.request;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ReqPushSettings {
    private Boolean pushMsgOrderExpired;
    private Boolean pushMsgOrderGrabed;
    private Boolean pushMsgOrderFinished;

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
}
