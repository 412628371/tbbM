package com.xinguang.tubobo.web.merchant.request;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/4/17.
 */
public class ReqOrderRiderLocation {
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
