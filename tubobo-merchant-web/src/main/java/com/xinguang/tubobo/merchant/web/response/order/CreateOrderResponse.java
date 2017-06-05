package com.xinguang.tubobo.merchant.web.response.order;

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

    @Override
    public String toString() {
        return "CreateOrderResponse{" +
                "orderNo='" + orderNo + '\'' +
                '}';
    }
}
