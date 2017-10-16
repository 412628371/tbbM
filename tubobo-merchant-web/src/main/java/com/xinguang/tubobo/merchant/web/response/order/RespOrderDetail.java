package com.xinguang.tubobo.merchant.web.response.order;

import java.io.Serializable;
import java.util.Date;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

/**
 * Created by Administrator on 2017/4/14.
 */
public class RespOrderDetail implements Serializable{
    private String cancelReason;//


    private String orderNo;//订单No
    private String orderStatus;//订单状态
    private String orderType;//订单类型
    private String orderRemarks;

    private Double payAmount;//支付总金额
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
    private Date expectFinishTime;//预计送达时间


    private String receiverName;//收货人姓名
    private String receiverPhone;//收货人联系方式
    private String receiverAddressProvince;//收货人详细地址
    private String receiverAddressCity;//收货人地址名称
    private String receiverAddressDistrict;//收货人地址名称
    private String receiverAddressStreet;//收货人地址名称
    private String receiverAddressDetail;//收货人地址名称
    private String receiverAddressRoomNo;//收货人地址名称
    private Double receiverLongitude;
    private Double receiverLatitude;

    private String riderName;
    private String riderPhone;

    private String commentContent;
    private Integer commentDeliveryScore;
    private Integer commentServiceScore;

    private Integer payExpiredMilSeconds;//过期间隔毫秒数

    private Integer grabExpiredMilSeconds;//抢单过期间隔毫秒数
    private Date   grabExpiredStartTime	;//抢单过期开始时间
    private Long grabRemainMillSeconds;
    private Long payRemainMillSeconds;

    private Boolean ratedFlag;
    private Double deliveryDistance;
    private  Double expiredMinute; //超时分钟数

    private Double expiredCompensation;//超时赔付expiredCompensation
    private  String waitPickCancelType; //带接单状态下商家取消订单原因

    private Double cancelCompensation; //订单取消赔付
    private Double cancelFine;         //订单取消罚款
    private String  unsettledReason;  //未妥投原因
    private Double  messageAmout;   //短信费用
    private String unsettledStatus; //未妥投状态
    private Date unsettledTime;             //驿站订单 商家确认未妥投时间
    private Double cancelSourceDeliveryFee; 	        //取消订单原发配送费
    private Double cancelSourceDeliverySubsidy ;      //取消订单原骑手补贴金额
    private String cancelSourceOrderNo ;  		    //取消订单原单号

    public Double getCancelSourceDeliveryFee() {
        return cancelSourceDeliveryFee;
    }

    public void setCancelSourceDeliveryFee(Double cancelSourceDeliveryFee) {
        this.cancelSourceDeliveryFee = cancelSourceDeliveryFee;
    }

    public Double getCancelSourceDeliverySubsidy() {
        return cancelSourceDeliverySubsidy;
    }

    public void setCancelSourceDeliverySubsidy(Double cancelSourceDeliverySubsidy) {
        this.cancelSourceDeliverySubsidy = cancelSourceDeliverySubsidy;
    }

    public String getCancelSourceOrderNo() {
        return cancelSourceOrderNo;
    }

    public void setCancelSourceOrderNo(String cancelSourceOrderNo) {
        this.cancelSourceOrderNo = cancelSourceOrderNo;
    }


    public Date getUnsettledTime() {
        return unsettledTime;
    }

    public void setUnsettledTime(Date unsettledTime) {
        this.unsettledTime = unsettledTime;
    }

    public String getUnsettledStatus() {
        return unsettledStatus;
    }

    public void setUnsettledStatus(String unsettledStatus) {
        this.unsettledStatus = unsettledStatus;
    }

    public String getUnsettledReason() {
        return unsettledReason;
    }

    public void setUnsettledReason(String unsettledReason) {
        this.unsettledReason = unsettledReason;
    }

    public Double getMessageAmout() {
        return messageAmout;
    }

    public void setMessageAmout(Double messageAmout) {
        this.messageAmout = messageAmout;
    }

    public Double getExpiredCompensation() {
        return expiredCompensation;
    }

    public void setExpiredCompensation(Double expiredCompensation) {
        this.expiredCompensation = expiredCompensation;
    }

    public String getWaitPickCancelType() {
        return waitPickCancelType;
    }

    public void setWaitPickCancelType(String waitPickCancelType) {
        this.waitPickCancelType = waitPickCancelType;
    }

    public Double getCancelCompensation() {
        return cancelCompensation;
    }

    public void setCancelCompensation(Double cancelCompensation) {
        this.cancelCompensation = cancelCompensation;
    }

    public Double getCancelFine() {
        return cancelFine;
    }

    public void setCancelFine(Double cancelFine) {
        this.cancelFine = cancelFine;
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

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
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


    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public void setReceiverLongitude(Double receiverLongitude) {
        this.receiverLongitude = receiverLongitude;
    }

    public void setReceiverLatitude(Double receiverLatitude) {
        this.receiverLatitude = receiverLatitude;
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

    public String getOrderRemarks() {
        return orderRemarks;
    }

    public void setOrderRemarks(String orderRemarks) {
        this.orderRemarks = orderRemarks;
    }

    public Long getGrabRemainMillSeconds() {
        return grabRemainMillSeconds;
    }

    public void setGrabRemainMillSeconds(Long grabRemainMillSeconds) {
        this.grabRemainMillSeconds = grabRemainMillSeconds;
    }

    public Long getPayRemainMillSeconds() {
        return payRemainMillSeconds;
    }

    public void setPayRemainMillSeconds(Long payRemainMillSeconds) {
        this.payRemainMillSeconds = payRemainMillSeconds;
    }


    public Date getExpectFinishTime() {
        return expectFinishTime;
    }

    public void setExpectFinishTime(Date expectFinishTime) {
        this.expectFinishTime = expectFinishTime;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Integer getCommentDeliveryScore() {
        return commentDeliveryScore;
    }

    public void setCommentDeliveryScore(Integer commentDeliveryScore) {
        this.commentDeliveryScore = commentDeliveryScore;
    }

    public Integer getCommentServiceScore() {
        return commentServiceScore;
    }

    public void setCommentServiceScore(Integer commentServiceScore) {
        this.commentServiceScore = commentServiceScore;
    }

    public Boolean getRatedFlag() {
        return ratedFlag;
    }

    public void setRatedFlag(Boolean ratedFlag) {
        this.ratedFlag = ratedFlag;
    }

    @Override
    public String toString() {
        return "RespOrderDetail{" +
                "cancelReason='" + cancelReason + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderRemarks='" + orderRemarks + '\'' +
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
                ", expectFinishTime=" + expectFinishTime +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverAddressProvince='" + receiverAddressProvince + '\'' +
                ", receiverAddressCity='" + receiverAddressCity + '\'' +
                ", receiverAddressDistrict='" + receiverAddressDistrict + '\'' +
                ", receiverAddressStreet='" + receiverAddressStreet + '\'' +
                ", receiverAddressDetail='" + receiverAddressDetail + '\'' +
                ", receiverAddressRoomNo='" + receiverAddressRoomNo + '\'' +
                ", receiverLongitude=" + receiverLongitude +
                ", receiverLatitude=" + receiverLatitude +
                ", riderName='" + riderName + '\'' +
                ", riderPhone='" + riderPhone + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", commentDeliveryScore=" + commentDeliveryScore +
                ", commentServiceScore=" + commentServiceScore +
                ", payExpiredMilSeconds=" + payExpiredMilSeconds +
                ", grabExpiredMilSeconds=" + grabExpiredMilSeconds +
                ", grabExpiredStartTime=" + grabExpiredStartTime +
                ", grabRemainMillSeconds=" + grabRemainMillSeconds +
                ", payRemainMillSeconds=" + payRemainMillSeconds +
                ", ratedFlag=" + ratedFlag +
                '}';
    }
}
