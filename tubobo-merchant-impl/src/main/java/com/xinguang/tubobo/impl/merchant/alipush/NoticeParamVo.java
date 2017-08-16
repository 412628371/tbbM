package com.xinguang.tubobo.impl.merchant.alipush;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NoticeParamVo implements Serializable{
    private String orderNo;
    private String id;
    private String orderType;
    private String reason;
    private String verifyResult;

    public String getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
