package com.xinguang.tubobo.merchant.web.request.shop;


import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/18.
 */
public class ReqMessageOperate implements Serializable {
    private Boolean messageOpen;

    public Boolean getMessageOpen() {
        return messageOpen;
    }

    public void setMessageOpen(Boolean messageOpen) {
        this.messageOpen = messageOpen;
    }
}
