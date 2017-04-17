package com.xinguang.tubobo.web.merchant.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/14.
 */
public class ShopIdentifyResponse implements Serializable {
    private String identifyStatus;

    public String getIdentifyStatus() {
        return identifyStatus;
    }

    public void setIdentifyStatus(String identifyStatus) {
        this.identifyStatus = identifyStatus;
    }
}
