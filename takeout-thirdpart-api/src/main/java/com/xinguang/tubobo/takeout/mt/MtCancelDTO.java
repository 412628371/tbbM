package com.xinguang.tubobo.takeout.mt;

import java.io.Serializable;

/**
 * 美团订单取消DTO.
 */
public class MtCancelDTO implements Serializable {
    private String originOrderId;
    private String reason;
    private String reasonCode;


    public String getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(String originOrderId) {
        this.originOrderId = originOrderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
