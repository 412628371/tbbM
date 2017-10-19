/**
 * 订单实体类;
 */
package com.xinguang.tubobo.impl.merchant.entity;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tubobo_order")
@DynamicInsert
@DynamicUpdate
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrderEntity extends BaseMerchantOrderEntity {

    private static final long serialVersionUID = 1L;
    //第三方
    private String platformCode;         //
    private String originOrderViewId;    //第三方平台原始展示订单号
    private String originOrderId;         //第三方平台原始订单号

    private Long payId;
    private String userId;//商家ID
    private String orderNo;//订单No
    private String orderStatus;//订单状态

    private Double payAmount;//支付总金额
    private Double deliveryFee;//配送费

    private Double tipFee;//小费
    private String payStatus;//支付状态
    private String payMethod;//支付方式
    private Date payTime;//付款时间

    private Date orderTime;//下单时间
    private Date cancelTime;//取消时间
    private Date grabOrderTime;//接单时间
    private Date grabItemTime;//取货时间
    private Date finishOrderTime;//送达时间
    private Date expectFinishTime;//预计送达时间
    private Date unsettledTime;             //驿站订单 商家确认未妥投时间



    //private String receiverId;//收货人id
    private String receiverName;//收货人姓名
    private String receiverPhone;//收货人联系方式
    private String receiverAddressProvince;//收货人详细地址
    private String receiverAddressCity;//收货人地址名称
    private String receiverAddressDistrict;//收货人地址名称
    private String receiverAddressStreet;//收货人地址名称
    private String receiverAddressDetail;//收货人地址名称
    private String receiverAddressRoomNo;//收货人地址名称
    private String senderAdcode;

    private Double receiverLongitude;
    private Double receiverLatitude;
    private Double deliveryDistance;

//	private Double dispatchRadius;//分派半径，单位：米

    private String riderId;
    private String riderName;
    private String riderPhone;
    private String orderType;//订单类型，大件或小件
    private Boolean ratedFlag;

    private Double peekOverFee;  //高峰溢价费用
    private Double weatherOverFee; //天气溢价费用


    private String unsettledStatus;         //驿站订单 未妥投状态  默认""  0:未妥投处理中 1：未妥投已处理

    private Long providerId;//所属服务商ID


    private String orderFeature;//订单特征值 RIDER_CANCEL_RESEND  骑手取消重发单
    private Boolean shortMessage;            //订单是否发送短信通知收件人
    private String senderName;//发货人名称

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Boolean getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(Boolean shortMessage) {
        this.shortMessage = shortMessage;
    }

    public Date getUnsettledTime() {
        return unsettledTime;
    }

    public void setUnsettledTime(Date unsettledTime) {
        this.unsettledTime = unsettledTime;
    }

    public String getOrderFeature() {
        return orderFeature;
    }

    public void setOrderFeature(String orderFeature) {
        this.orderFeature = orderFeature;
    }

    public String getSenderAdcode() {
        return senderAdcode;
    }

    public void setSenderAdcode(String senderAdcode) {
        this.senderAdcode = senderAdcode;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getOriginOrderViewId() {
        return originOrderViewId;
    }

    public void setOriginOrderViewId(String originOrderViewId) {
        this.originOrderViewId = originOrderViewId;
    }

    public String getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(String originOrderId) {
        this.originOrderId = originOrderId;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getReceiverAddressRoomNo() {
        return receiverAddressRoomNo;
    }

    public void setReceiverAddressRoomNo(String receiverAddressRoomNo) {
        this.receiverAddressRoomNo = receiverAddressRoomNo;
    }

    public Double getReceiverLongitude() {
        return receiverLongitude;
    }

    public void setReceiverLongitude(Double receiverLongitude) {
        this.receiverLongitude = receiverLongitude;
    }

    public Double getReceiverLatitude() {
        return receiverLatitude;
    }

    public void setReceiverLatitude(Double receiverLatitude) {
        this.receiverLatitude = receiverLatitude;
    }

    public Double getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(Double deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Boolean getRatedFlag() {
        return ratedFlag;
    }

    public void setRatedFlag(Boolean ratedFlag) {
        this.ratedFlag = ratedFlag;
    }

    public Double getPeekOverFee() {
        return peekOverFee;
    }

    public void setPeekOverFee(Double peekOverFee) {
        this.peekOverFee = peekOverFee;
    }

    public Double getWeatherOverFee() {
        return weatherOverFee;
    }

    public void setWeatherOverFee(Double weatherOverFee) {
        this.weatherOverFee = weatherOverFee;
    }

    public String getUnsettledStatus() {
        return unsettledStatus;
    }

    public void setUnsettledStatus(String unsettledStatus) {
        this.unsettledStatus = unsettledStatus;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

}