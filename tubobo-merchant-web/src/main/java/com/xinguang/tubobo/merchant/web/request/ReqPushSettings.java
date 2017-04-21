package com.xinguang.tubobo.merchant.web.request;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ReqPushSettings {
    private String deviceToken;
    private Boolean pushMsgOrderExpired;
    private Boolean pushMsgOrderGrabed;
    private Boolean pushMsgOrderFinished;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    @Override
    public String toString() {
        return "ReqPushSettings{" +
                "deviceToken='" + deviceToken + '\'' +
                ", pushMsgOrderExpired=" + pushMsgOrderExpired +
                ", pushMsgOrderGrabed=" + pushMsgOrderGrabed +
                ", pushMsgOrderFinished=" + pushMsgOrderFinished +
                '}';
    }
}
