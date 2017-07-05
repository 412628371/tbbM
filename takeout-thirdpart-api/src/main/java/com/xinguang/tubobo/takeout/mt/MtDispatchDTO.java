package com.xinguang.tubobo.takeout.mt;

import java.io.Serializable;

/**
 * 美团配送状态DTO.
 */
public class MtDispatchDTO implements Serializable {
    private String dispatcherMobile;
    private String dispatcherName;
    private String originOrderId;
    private String shippingStatus;


    public String getDispatcherMobile() {
        return dispatcherMobile;
    }

    public void setDispatcherMobile(String dispatcherMobile) {
        this.dispatcherMobile = dispatcherMobile;
    }

    public String getDispatcherName() {
        return dispatcherName;
    }

    public void setDispatcherName(String dispatcherName) {
        this.dispatcherName = dispatcherName;
    }

    public String getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(String originOrderId) {
        this.originOrderId = originOrderId;
    }

    public String getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(String shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
}
