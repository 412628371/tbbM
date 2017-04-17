package com.xinguang.tubobo.web.merchant.request;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ReqAccountPay {
    @NotBlank(message = "订单编号不存在")
   private String orderNo;
   private String payMethod;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
