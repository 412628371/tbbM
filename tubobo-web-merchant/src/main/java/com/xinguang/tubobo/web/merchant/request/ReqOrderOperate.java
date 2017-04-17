package com.xinguang.tubobo.web.merchant.request;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ReqOrderOperate {
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;
    @NotBlank(message = "操作类型不能为空")
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
