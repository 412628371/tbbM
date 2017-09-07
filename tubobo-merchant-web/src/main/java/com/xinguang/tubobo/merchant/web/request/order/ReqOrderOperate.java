package com.xinguang.tubobo.merchant.web.request.order;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ReqOrderOperate {
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;
    @NotBlank(message = "操作类型不能为空")
    private String command;
    private String waitPickCancelType; //已接单状态下取消原因,为null时说明是待接单状态下取消

    public String getWaitPickCancelType() {
        return waitPickCancelType;
    }

    public void setWaitPickCancelType(String waitPickCancelType) {
        this.waitPickCancelType = waitPickCancelType;
    }

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

    @Override
    public String toString() {
        return "ReqOrderOperate{" +
                "orderNo='" + orderNo + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
