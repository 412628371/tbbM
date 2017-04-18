/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;
import java.util.Date;

public class MerchantOrderDTO implements Serializable{

	private String orderNo;//订单No
	private String orderType;
	private String orderTitle;

	private Integer payAmount;//支付总金额
	private Integer deliveryFee;//配送费
	private Integer tipFee;//小费

	private Long payId;//支付ID

	private Integer expireSeconds;//过期时间 单位秒

	private String senderId;//发货人id
	private String senderName;//发货人名称
	private String senderPhone;//发货人联系方式
	private String senderAddressProvince;//
	private String senderAddressCity;//
	private String senderAddressDistrict;//
	private String senderAddressStreet;//
	private String senderAddressDetail;//
	private String senderAddress;//发货人地址
	private Double senderLongitude;
	private Double senderLatitude;

	private String receiverId;//收货人id
	private String receiverName;//收货人姓名
	private String receiverPhone;//收货人联系方式
	private String receiverAddressProvince;//收货人省
	private String receiverAddressCity;//收货人市
	private String receiverAddressDistrict;//收货人区
	private String receiverAddressStreet;//收货人街道
	private String receiverAddressDetail;//收货人市详细地址
	private Double receiverLongitude;
	private Double receiverLatitude;

	private Double dispatchRadius;//分派半径，单位：米

	private Date orderTime;//下单时间
	private Date cancelTime;//取消时间
	private Date closeTime;//关闭时间
	private Date expiredTime;//超时时间
	private Date grabOrderTime;//接单时间
	private Date grabItemTime;//取货时间
	private Date finishOrderTime;//送达时间
	protected Date createDate;
	protected Date updateDate;

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

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
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

	public Integer getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(Integer expireSeconds) {
		this.expireSeconds = expireSeconds;
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

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
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
}