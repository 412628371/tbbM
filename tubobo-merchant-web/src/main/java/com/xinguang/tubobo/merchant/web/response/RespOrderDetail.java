package com.xinguang.tubobo.merchant.web.response;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14.
 */
public class RespOrderDetail implements Serializable{
    private String cancelReason;//

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    private String orderNo;//订单No
    private String orderStatus;//订单状态
    private String orderRemark;

    private int payAmount;//支付总金额
    private Double deliveryFee;//配送费
    private Double tipFee;//小费
    private String payStatus;//支付状态
    private String payMethod;//支付方式
    private Date payTime;//付款时间

    private Date orderTime;//下单时间
    private Date cancelTime;//取消时间
    private Date closeTime;//关闭时间
    private Date expiredTime;//超时时间
    private Date grabOrderTime;//接单时间
    private Date grabItemTime;//取货时间
    private Date finishOrderTime;//送达时间


    private String receiverName;//收货人姓名
    private String receiverPhone;//收货人联系方式
    private String receiverAddressProvince;//收货人详细地址
    private String receiverAddressCity;//收货人地址名称
    private String receiverAddressDistrict;//收货人地址名称
    private String receiverAddressStreet;//收货人地址名称
    private String receiverAddressDetail;//收货人地址名称
    private String receiverAddressRoomNo;//收货人地址名称
    private double receiverLongitude;
    private double receiverLatitude;

    private String riderName;
    private String riderPhone;

    private Integer payExpiredMilSeconds;//过期间隔毫秒数

    private Integer grabExpiredMilSeconds;//抢单过期间隔毫秒数
    private Date   grabExpiredStartTime	;//抢单过期开始时间

    public Integer getPayExpiredMilSeconds() {
        return payExpiredMilSeconds;
    }

    public void setPayExpiredMilSeconds(Integer payExpiredMilSeconds) {
        this.payExpiredMilSeconds = payExpiredMilSeconds;
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

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public int getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(int payAmount) {
        this.payAmount = payAmount;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Double getTipFee() {
        return tipFee;
    }

    public void setTipFee(Double tipFee) {
        this.tipFee = tipFee;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddressProvince() {
        return receiverAddressProvince;
    }

    public void setReceiverAddressProvince(String receiverAddressProvince) {
        this.receiverAddressProvince = receiverAddressProvince;
    }

    public String getReceiverAddressCity() {
        return receiverAddressCity;
    }

    public void setReceiverAddressCity(String receiverAddressCity) {
        this.receiverAddressCity = receiverAddressCity;
    }

    public String getReceiverAddressDistrict() {
        return receiverAddressDistrict;
    }

    public void setReceiverAddressDistrict(String receiverAddressDistrict) {
        this.receiverAddressDistrict = receiverAddressDistrict;
    }

    public String getReceiverAddressStreet() {
        return receiverAddressStreet;
    }

    public void setReceiverAddressStreet(String receiverAddressStreet) {
        this.receiverAddressStreet = receiverAddressStreet;
    }

    public String getReceiverAddressDetail() {
        return receiverAddressDetail;
    }

    public void setReceiverAddressDetail(String receiverAddressDetail) {
        this.receiverAddressDetail = receiverAddressDetail;
    }

    public double getReceiverLongitude() {
        return receiverLongitude;
    }

    public void setReceiverLongitude(double receiverLongitude) {
        this.receiverLongitude = receiverLongitude;
    }

    public double getReceiverLatitude() {
        return receiverLatitude;
    }

    public void setReceiverLatitude(double receiverLatitude) {
        this.receiverLatitude = receiverLatitude;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }

    public Integer getGrabExpiredMilSeconds() {
        return grabExpiredMilSeconds;
    }

    public void setGrabExpiredMilSeconds(Integer grabExpiredMilSeconds) {
        this.grabExpiredMilSeconds = grabExpiredMilSeconds;
    }

    public Date getGrabExpiredStartTime() {
        return grabExpiredStartTime;
    }

    public void setGrabExpiredStartTime(Date grabExpiredStartTime) {
        this.grabExpiredStartTime = grabExpiredStartTime;
    }

    public String getReceiverAddressRoomNo() {
        return receiverAddressRoomNo;
    }

    public void setReceiverAddressRoomNo(String receiverAddressRoomNo) {
        this.receiverAddressRoomNo = receiverAddressRoomNo;
    }

    @Override
    public String toString() {
        return "RespOrderDetail{" +
                "orderNo='" + orderNo + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderRemark='" + orderRemark + '\'' +
                ", payAmount=" + payAmount +
                ", deliveryFee=" + deliveryFee +
                ", tipFee=" + tipFee +
                ", payStatus='" + payStatus + '\'' +
                ", payMethod='" + payMethod + '\'' +
                ", payTime=" + payTime +
                ", orderTime=" + orderTime +
                ", cancelTime=" + cancelTime +
                ", closeTime=" + closeTime +
                ", expiredTime=" + expiredTime +
                ", grabOrderTime=" + grabOrderTime +
                ", grabItemTime=" + grabItemTime +
                ", finishOrderTime=" + finishOrderTime +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverAddressProvince='" + receiverAddressProvince + '\'' +
                ", receiverAddressCity='" + receiverAddressCity + '\'' +
                ", receiverAddressDistrict='" + receiverAddressDistrict + '\'' +
                ", receiverAddressStreet='" + receiverAddressStreet + '\'' +
                ", receiverAddressDetail='" + receiverAddressDetail + '\'' +
                ", receiverLongitude=" + receiverLongitude +
                ", receiverLatitude=" + receiverLatitude +
                ", riderName='" + riderName + '\'' +
                ", riderPhone='" + riderPhone + '\'' +
                ", payExpiredMilSeconds=" + payExpiredMilSeconds +
                ", grabExpiredMilSeconds=" + grabExpiredMilSeconds +
                ", grabExpiredStartTime=" + grabExpiredStartTime +
                '}';
    }
}
