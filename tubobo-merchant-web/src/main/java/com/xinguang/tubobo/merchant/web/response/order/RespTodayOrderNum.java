package com.xinguang.tubobo.merchant.web.response.order;

/**
 * Created by Administrator on 2017/5/16.
 */
public class RespTodayOrderNum {
    private Long finishOrders;
    private Long sendMessages;

    public Long getSendMessages() {
        return sendMessages;
    }

    public void setSendMessages(Long sendMessages) {
        this.sendMessages = sendMessages;
    }

    public Long getFinishOrders() {
        return finishOrders;
    }

    public void setFinishOrders(Long finishOrders) {
        this.finishOrders = finishOrders;
    }
}
