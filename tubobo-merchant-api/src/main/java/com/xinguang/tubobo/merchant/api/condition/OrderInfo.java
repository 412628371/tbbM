package com.xinguang.tubobo.merchant.api.condition;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yangxb on 2017/9/11.
 */
public class OrderInfo implements Serializable{

    private String cancelReason;//PAY_MERCHANT-未支付商家取消；PAY_OVERTIME：超时未付款自动取消；GRAB_MERCHANT：未接单商家取消；GRAB_OVERTIME：超时未接单自动取消；ADMIN_CANCEL: 后台取消
    private Date cancelTime;//取消时间
    private String deliveryDistance; //配送距离 单位m
    private Date expectFinishTime;//预计送达时间
    private Double expiredMinute;// 订单送达超时时间 v1.41 加入 0或者null代表未超时 单位分
    private Date finishOrderTime;//送达时间
    private Date grabItemTime;//取货时间
    private Date grabOrderTime;//接单时间
    private String orderNo;//订单No
    private String orderRemarks;
    private String orderStatus;//INIT:待付款，WAITING_GRAB:待接单，WAITING_PICK:待取货，DELIVERYING:配送中，UNDELIVERED未妥投， FINISH：已完成，CANCEL：已取消
    private Date orderTime;//下单时间
    private String orderType;//小件订单：smallOrder;大件订单：bigOrder

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(String deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public Date getExpectFinishTime() {
        return expectFinishTime;
    }

    public void setExpectFinishTime(Date expectFinishTime) {
        this.expectFinishTime = expectFinishTime;
    }

    public Double getExpiredMinute() {
        return expiredMinute;
    }

    public void setExpiredMinute(Double expiredMinute) {
        this.expiredMinute = expiredMinute;
    }

    public Date getFinishOrderTime() {
        return finishOrderTime;
    }

    public void setFinishOrderTime(Date finishOrderTime) {
        this.finishOrderTime = finishOrderTime;
    }

    public Date getGrabItemTime() {
        return grabItemTime;
    }

    public void setGrabItemTime(Date grabItemTime) {
        this.grabItemTime = grabItemTime;
    }

    public Date getGrabOrderTime() {
        return grabOrderTime;
    }

    public void setGrabOrderTime(Date grabOrderTime) {
        this.grabOrderTime = grabOrderTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
