package com.xinguang.tubobo.merchant.web.request;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ReqAccountPay {
    @NotBlank(message = "订单编号不能为空")
   private String orderNo;
   private String payMethod;
    private String payPassword;

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayMethod() {
        return payMethod;
    }

    @Override
    public String toString() {
        return "ReqAccountPay{" +
                "orderNo='" + orderNo + '\'' +
                ", payMethod='" + payMethod + '\'' +
                '}';
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
