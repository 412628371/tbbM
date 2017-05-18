/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.entity;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tubobo_merchant_order")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MerchantOrderEntity extends BaseMerchantEntity {

	private static final long serialVersionUID = 1L;

	private String cancelReason;
	private Long payId;
	private String userId;//商家ID
	private String orderNo;//订单No
	private String orderStatus;//订单状态
	private String orderType;
	private String orderRemark;

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

	private String senderId;//发货人id
	private String senderName;//发货人名称
	private String senderPhone;//发货人联系方式
	private String senderAddressProvince;//收货人详细地址
	private String senderAddressCity;//寄货人地址名称
	private String senderAddressDistrict;//寄货人地址名称
	private String senderAddressStreet;//寄货人地址名称
	private String senderAddressDetail;//寄货人地址名称
	private String senderAddressRoomNo;//寄货人地址名称
	private Double senderLongitude;
	private Double senderLatitude;

	private String receiverId;//收货人id
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
	private Double deliveryDistance;

	private Double dispatchRadius;//分派半径，单位：米

	private String riderId;
	private String riderName;
	private String riderPhone;

	private Boolean ratedFlag;
	public String getReceiverAddressRoomNo() {
		return receiverAddressRoomNo;
	}

	public void setReceiverAddressRoomNo(String receiverAddressRoomNo) {
		this.receiverAddressRoomNo = receiverAddressRoomNo;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Double getDeliveryFee() {
		if (deliveryFee == null){
			return 0.0;
		}
		return deliveryFee;
	}

	public void setDeliveryFee(Double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public Double getTipFee() {
		if (tipFee == null){
			return 0.0;
		}
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

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = ConvertUtil.handleNullString(senderName);
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getSenderAddressProvince() {
		return senderAddressProvince;
	}

	public void setSenderAddressProvince(String senderAddressProvince) {
		this.senderAddressProvince = ConvertUtil.handleNullString(senderAddressProvince);
	}

	public String getSenderAddressCity() {
		return senderAddressCity;
	}

	public void setSenderAddressCity(String senderAddressCity) {
		this.senderAddressCity = ConvertUtil.handleNullString(senderAddressCity);
	}

	public String getSenderAddressDistrict() {
		return senderAddressDistrict;
	}

	public void setSenderAddressDistrict(String senderAddressDistrict) {
		this.senderAddressDistrict = ConvertUtil.handleNullString(senderAddressDistrict);
	}

	public String getSenderAddressStreet() {
		return senderAddressStreet;
	}

	public void setSenderAddressStreet(String senderAddressStreet) {
		this.senderAddressStreet = ConvertUtil.handleNullString(senderAddressStreet);
	}

	public String getSenderAddressDetail() {
		return senderAddressDetail;
	}

	public void setSenderAddressDetail(String senderAddressDetail) {
		this.senderAddressDetail = ConvertUtil.handleNullString(senderAddressDetail);
	}

	public Double getSenderLongitude() {
		return senderLongitude;
	}

	public void setSenderLongitude(Double senderLongitude) {
		this.senderLongitude = senderLongitude;
	}

	public Double getSenderLatitude() {
		return senderLatitude;
	}

	public void setSenderLatitude(Double senderLatitude) {
		this.senderLatitude = senderLatitude;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
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
		this.receiverAddressDistrict = ConvertUtil.handleNullString(receiverAddressDistrict);
	}

	public String getReceiverAddressStreet() {
		return receiverAddressStreet;
	}

	public void setReceiverAddressStreet(String receiverAddressStreet) {
		this.receiverAddressStreet = ConvertUtil.handleNullString(receiverAddressStreet);
	}

	public String getReceiverAddressDetail() {
		return receiverAddressDetail;
	}

	public void setReceiverAddressDetail(String receiverAddressDetail) {
		this.receiverAddressDetail = ConvertUtil.handleNullString(receiverAddressDetail);
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

	public Double getDispatchRadius() {
		return dispatchRadius;
	}

	public void setDispatchRadius(Double dispatchRadius) {
		this.dispatchRadius = dispatchRadius;
	}

	public String getRiderId() {
		return riderId;
	}

	public void setRiderId(String riderId) {
		this.riderId = ConvertUtil.handleNullString(riderId);
	}

	public String getRiderName() {
		return riderName;
	}

	public void setRiderName(String riderName) {
		this.riderName = ConvertUtil.handleNullString(riderName);
	}

	public String getRiderPhone() {
		return riderPhone;
	}

	public void setRiderPhone(String riderPhone) {
		this.riderPhone = ConvertUtil.handleNullString(riderPhone);
	}

	public Double getDeliveryDistance() {
		return deliveryDistance;
	}

	public void setDeliveryDistance(Double deliveryDistance) {
		this.deliveryDistance = deliveryDistance;
	}

	public String getSenderAddressRoomNo() {
		return senderAddressRoomNo;
	}

	public void setSenderAddressRoomNo(String senderAddressRoomNo) {
		this.senderAddressRoomNo = senderAddressRoomNo;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Date getExpectFinishTime() {
		return expectFinishTime;
	}

	public void setExpectFinishTime(Date expectFinishTime) {
		this.expectFinishTime = expectFinishTime;
	}

	public Boolean getRatedFlag() {
		return ratedFlag;
	}

	public void setRatedFlag(Boolean ratedFlag) {
		this.ratedFlag = ratedFlag;
	}
}