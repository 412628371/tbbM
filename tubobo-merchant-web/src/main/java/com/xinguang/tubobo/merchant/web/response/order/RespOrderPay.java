package com.xinguang.tubobo.merchant.web.response.order;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/21.
 */
public class RespOrderPay {
    private Date grabExpiredStartTime;
    private Integer grabExpiredMilSeconds;

    public Date getGrabExpiredStartTime() {
        return grabExpiredStartTime;
    }

    public void setGrabExpiredStartTime(Date grabExpiredStartTime) {
        this.grabExpiredStartTime = grabExpiredStartTime;
    }

    public Integer getGrabExpiredMilSeconds() {
        return grabExpiredMilSeconds;
    }

    public void setGrabExpiredMilSeconds(Integer grabExpiredMilSeconds) {
        this.grabExpiredMilSeconds = grabExpiredMilSeconds;
    }
}
