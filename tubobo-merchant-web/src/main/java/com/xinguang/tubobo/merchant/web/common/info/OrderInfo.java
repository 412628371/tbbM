package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单信息.
 */
public class OrderInfo implements Serializable {
    private String orderNo;//订单No
    private String orderType;//订单类型
    private String orderStatus;//订单状态
    private String orderRemarks;
    private String cancelReason;
    private Date orderTime;//下单时间
    private Date cancelTime;//取消时间
    private Date closeTime;//关闭时间
    private Date expiredTime;//超时时间
    private Date grabOrderTime;//接单时间
    private Date grabItemTime;//取货时间
    private Date finishOrderTime;//送达时间
    private Date expectFinishTime;//预计送达时间
    private Double expiredMinute;//超时时间(min)

    private String unsettledStatus;         //驿站订单 未妥投状态  默认""  0:未妥投处理中 1：未妥投已处理


    private Long grabRemainMillSeconds;
    private Double deliveryDistance; //配送距离 1.41 版本加入

    private  String waitPickCancelType; //带接单状态下商家取消订单原因
    private  String  unsettledReason;  //未妥投原因
    private Date unsettledTime;        //驿站订单 商家确认未妥投时间

    public Date getUnsettledTime() {
        return unsettledTime;
    }

    public void setUnsettledTime(Date unsettledTime) {
        this.unsettledTime = unsettledTime;
    }


    public String getUnsettledReason() {
        return unsettledReason;
    }

    public void setUnsettledReason(String unsettledReason) {
        this.unsettledReason = unsettledReason;
    }

    public String getUnsettledStatus() {
        return unsettledStatus;
    }

    public void setUnsettledStatus(String unsettledStatus) {
        this.unsettledStatus = unsettledStatus;
    }

    public String getWaitPickCancelType() {
        return waitPickCancelType;
    }

    public void setWaitPickCancelType(String waitPickCancelType) {
        this.waitPickCancelType = waitPickCancelType;
    }





    public Double getExpiredMinute() {
        return expiredMinute;
    }

    public void setExpiredMinute(Double expiredMinute) {
        this.expiredMinute = expiredMinute;
    }

    public Double getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(Double deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Date getGrabOrderTime() {
        return grabOrderTime;
    }

    public void setGrabOrderTime(Date grabOrderTime) {
        this.grabOrderTime = grabOrderTime;
    }

    public Date getGrabItemTime() {
        return grabItemTime;
    }

    public void setGrabItemTime(Date grabItemTime) {
        this.grabItemTime = grabItemTime;
    }

    public Date getFinishOrderTime() {
        return finishOrderTime;
    }

    public void setFinishOrderTime(Date finishOrderTime) {
        this.finishOrderTime = finishOrderTime;
    }

    public Date getExpectFinishTime() {
        return expectFinishTime;
    }

    public void setExpectFinishTime(Date expectFinishTime) {
        this.expectFinishTime = expectFinishTime;
    }

    public Long getGrabRemainMillSeconds() {
        return grabRemainMillSeconds;
    }

    public void setGrabRemainMillSeconds(Long grabRemainMillSeconds) {
        this.grabRemainMillSeconds = grabRemainMillSeconds;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
