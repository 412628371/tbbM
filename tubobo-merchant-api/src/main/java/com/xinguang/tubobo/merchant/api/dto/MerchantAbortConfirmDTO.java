package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by yangxb on 2017/9/11.
 */
public class MerchantAbortConfirmDTO implements Serializable{
    private String orderNo;//订单号	string	Y	　
    private Boolean confirm;//是否确认	boolean	Y	　
    private String message;//信息	string	　
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
