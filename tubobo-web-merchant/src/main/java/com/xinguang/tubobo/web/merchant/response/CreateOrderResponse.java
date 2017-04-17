package com.xinguang.tubobo.web.merchant.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/14.
 */
public class CreateOrderResponse implements Serializable{
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
