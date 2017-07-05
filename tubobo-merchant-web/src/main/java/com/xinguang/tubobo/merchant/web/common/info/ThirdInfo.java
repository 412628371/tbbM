package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/3.
 */
public class ThirdInfo implements Serializable {
    private String platformCode;
    private String originOrderId;
    private String originOrderViewId;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(String originOrderId) {
        this.originOrderId = originOrderId;
    }

    public String getOriginOrderViewId() {
        return originOrderViewId;
    }

    public void setOriginOrderViewId(String originOrderViewId) {
        this.originOrderViewId = originOrderViewId;
    }

    @Override
    public String toString() {
        return "ThirdInfo{" +
                "platformCode='" + platformCode + '\'' +
                ", originOrderId='" + originOrderId + '\'' +
                ", originOrderViewId='" + originOrderViewId + '\'' +
                '}';
    }
}
