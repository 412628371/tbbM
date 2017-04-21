package com.xinguang.tubobo.merchant.web.response;

import java.util.Date;

/**
 * Created by Administrator on 2017/4/21.
 */
public class RespOrderPay {
    private Date orderExpiredStartTime;
    private Integer expiredMilSeconds;

    public Date getOrderExpiredStartTime() {
        return orderExpiredStartTime;
    }

    public void setOrderExpiredStartTime(Date orderExpiredStartTime) {
        this.orderExpiredStartTime = orderExpiredStartTime;
    }

    public Integer getExpiredMilSeconds() {
        return expiredMilSeconds;
    }

    public void setExpiredMilSeconds(Integer expiredMilSeconds) {
        this.expiredMilSeconds = expiredMilSeconds;
    }
}
