/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MerchantOrderDTO implements Serializable{

	private String cancelReason;
	private Long payId;
	private String userId;//商家ID
	private String orderNo;//订单No
	private String orderStatus;//订单状态
	private String orderType;
	private String orderRemark;

	private Integer expireMilSeconds;//过期毫秒数
	private Integer payAmount;//支付总金额
	private Integer deliveryFee;//配送费
	private Integer tipFee;//小费
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
	private String  senderAvatar;

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
	private String riderCarNo;
	private String carType;//车辆类型
	private String carTypeName;//车辆类型名称

	private Date appointTime;	//预约时间
	private String appointType; //预约类型

	protected Date createDate;
	protected String createBy;
	protected Date updateDate;
	protected String updateBy;
	protected String senderAdcode;

	private Integer peekOverFee;  //高峰溢价费用
	private Integer weatherOverFee; //天气溢价费用

	public Integer getPeekOverFee() {
		return peekOverFee;
	}

	public void setPeekOverFee(Integer peekOverFee) {
		this.peekOverFee = peekOverFee;
	}

	public Integer getWeatherOverFee() {
		return weatherOverFee;
	}

	public void setWeatherOverFee(Integer weatherOverFee) {
		this.weatherOverFee = weatherOverFee;
	}

	public Date getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
	}

	public String getAppointType() {
		return appointType;
	}

	public void setAppointType(String appointType) {
		this.appointType = appointType;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarTypeName() {
		return carTypeName;
	}

	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}


	public Integer getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Integer payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(Integer deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public Integer getTipFee() {
		return tipFee;
	}

	public void setTipFee(Integer tipFee) {
		this.tipFee = tipFee;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderRemark() {
		return orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Integer getExpireMilSeconds() {
		return expireMilSeconds;
	}

	public void setExpireMilSeconds(Integer expireMilSeconds) {
		this.expireMilSeconds = expireMilSeconds;
	}

	public Double getDeliveryDistance() {
		return deliveryDistance;
	}

	public void setDeliveryDistance(Double deliveryDistance) {
		this.deliveryDistance = deliveryDistance;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
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
		this.senderName = senderName;
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
		this.senderAddressProvince = senderAddressProvince;
	}

	public String getSenderAddressCity() {
		return senderAddressCity;
	}

	public void setSenderAddressCity(String senderAddressCity) {
		this.senderAddressCity = senderAddressCity;
	}

	public String getSenderAddressDistrict() {
		return senderAddressDistrict;
	}

	public void setSenderAddressDistrict(String senderAddressDistrict) {
		this.senderAddressDistrict = senderAddressDistrict;
	}

	public String getSenderAddressStreet() {
		return senderAddressStreet;
	}

	public void setSenderAddressStreet(String senderAddressStreet) {
		this.senderAddressStreet = senderAddressStreet;
	}

	public String getSenderAddressDetail() {
		return senderAddressDetail;
	}

	public void setSenderAddressDetail(String senderAddressDetail) {
		this.senderAddressDetail = senderAddressDetail;
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

	public String getSenderAddressRoomNo() {
		return senderAddressRoomNo;
	}

	public void setSenderAddressRoomNo(String senderAddressRoomNo) {
		this.senderAddressRoomNo = senderAddressRoomNo;
	}

	public String getSenderAvatar() {
		return senderAvatar;
	}

	public void setSenderAvatar(String senderAvatar) {
		this.senderAvatar = senderAvatar;
	}

	public String getReceiverAddressRoomNo() {
		return receiverAddressRoomNo;
	}

	public void setReceiverAddressRoomNo(String receiverAddressRoomNo) {
		this.receiverAddressRoomNo = receiverAddressRoomNo;
	}

	public String getRiderCarNo() {
		return riderCarNo;
	}

	public void setRiderCarNo(String riderCarNo) {
		this.riderCarNo = riderCarNo;
	}

	public String getSenderAdcode() {
		return senderAdcode;
	}

	public void setSenderAdcode(String senderAdcode) {
		this.senderAdcode = senderAdcode;
	}

	@Override
	public String toString() {
		return "MerchantOrderDTO{" +
				"cancelReason='" + cancelReason + '\'' +
				", payId=" + payId +
				", userId='" + userId + '\'' +
				", orderNo='" + orderNo + '\'' +
				", orderStatus='" + orderStatus + '\'' +
				", orderType='" + orderType + '\'' +
				", orderRemark='" + orderRemark + '\'' +
				", expireMilSeconds=" + expireMilSeconds +
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
				", senderId='" + senderId + '\'' +
				", senderName='" + senderName + '\'' +
				", senderPhone='" + senderPhone + '\'' +
				", senderAddressProvince='" + senderAddressProvince + '\'' +
				", senderAddressCity='" + senderAddressCity + '\'' +
				", senderAddressDistrict='" + senderAddressDistrict + '\'' +
				", senderAddressStreet='" + senderAddressStreet + '\'' +
				", senderAddressDetail='" + senderAddressDetail + '\'' +
				", senderAddressRoomNo='" + senderAddressRoomNo + '\'' +
				", senderLongitude=" + senderLongitude +
				", senderLatitude=" + senderLatitude +
				", senderAvatar='" + senderAvatar + '\'' +
				", receiverId='" + receiverId + '\'' +
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
				", deliveryDistance=" + deliveryDistance +
				", dispatchRadius=" + dispatchRadius +
				", riderId='" + riderId + '\'' +
				", riderName='" + riderName + '\'' +
				", riderPhone='" + riderPhone + '\'' +
				", createDate=" + createDate +
				", createBy='" + createBy + '\'' +
				", updateDate=" + updateDate +
				", updateBy='" + updateBy + '\'' +
				'}';
	}
}