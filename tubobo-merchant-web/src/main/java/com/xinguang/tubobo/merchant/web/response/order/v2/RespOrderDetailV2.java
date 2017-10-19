package com.xinguang.tubobo.merchant.web.response.order.v2;

import com.xinguang.tubobo.impl.merchant.common.AddressInfo;
import com.xinguang.tubobo.merchant.web.common.info.*;

import java.io.Serializable;

/**
 * 订单详情v2.0.
 */
public class RespOrderDetailV2  implements Serializable{
    private OrderInfo orderInfo;
    private PayInfo payInfo;
    private CommentsInfo commentsInfo;
    private DriverInfo driverInfo;


    private AddressInfo consignor;
    private AddressInfo receiver;

    private CarInfo carInfo;

    private AppointTask appointTask;

    private OverFeeInfo overFeeInfo;
    private ThirdInfo thirdInfo;

    public OverFeeInfo getOverFeeInfo() {
        return overFeeInfo;
    }

    public void setOverFeeInfo(OverFeeInfo overFeeInfo) {
        this.overFeeInfo = overFeeInfo;
    }

    public AppointTask getAppointTask() {
        return appointTask;
    }

    public void setAppointTask(AppointTask appointTask) {
        this.appointTask = appointTask;
    }

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }

    public CommentsInfo getCommentsInfo() {
        return commentsInfo;
    }

    public void setCommentsInfo(CommentsInfo commentsInfo) {
        this.commentsInfo = commentsInfo;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public AddressInfo getConsignor() {
        return consignor;
    }

    public void setConsignor(AddressInfo consignor) {
        this.consignor = consignor;
    }

    public AddressInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(AddressInfo receiver) {
        this.receiver = receiver;
    }

    public ThirdInfo getThirdInfo() {
        return thirdInfo;
    }

    public void setThirdInfo(ThirdInfo thirdInfo) {
        this.thirdInfo = thirdInfo;
    }
}
