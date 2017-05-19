package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/19.
 */
public class MerchantFinishCallbackDTO implements Serializable {
    private String orderNo;
    private Date finishOrderTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getFinishOrderTime() {
        return finishOrderTime;
    }

    public void setFinishOrderTime(Date finishOrderTime) {
        this.finishOrderTime = finishOrderTime;
    }
}
