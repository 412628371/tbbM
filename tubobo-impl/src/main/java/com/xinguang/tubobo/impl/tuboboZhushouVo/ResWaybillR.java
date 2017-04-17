package com.xinguang.tubobo.impl.tuboboZhushouVo;


public class ResWaybillR {
	
    private String id;

    private String waybillNo; // 运单号
	private Integer waybillStatus;

    private String taskStatus;
    
    private String senderName; // 寄件人姓名
    private String senderPhone; // 寄件人手机号
    private String senderPcdCode;//省市区代码
    private String senderPcdName;//省市区中文
    private String senderAddress; // 寄件人详细地址
    private String receiverName; // 收件人姓名
    private String receiverPhone; // 收件人手机号
    private String receiverPcdCode;//省市区代码
    private String receiverPcdName;//省市区中文
    private String receiverAddress; // 收件人详细地址
    
    private String customerType;//寄件客户类型
    
    private String shipment;//寄件物
    private Boolean insureFlag;//是否保价
    private Double insurePrice;//保价金额 单位：元；数字
    private Double weight;//单位：KG；数字
    private String remark;
    private String source;//来源
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public Integer getWaybillStatus() {
		return waybillStatus;
	}
	public void setWaybillStatus(Integer waybillStatus) {
		this.waybillStatus = waybillStatus;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
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
	public String getSenderPcdCode() {
		return senderPcdCode;
	}
	public void setSenderPcdCode(String senderPcdCode) {
		this.senderPcdCode = senderPcdCode;
	}
	public String getSenderPcdName() {
		return senderPcdName;
	}
	public void setSenderPcdName(String senderPcdName) {
		this.senderPcdName = senderPcdName;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
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
	public String getReceiverPcdCode() {
		return receiverPcdCode;
	}
	public void setReceiverPcdCode(String receiverPcdCode) {
		this.receiverPcdCode = receiverPcdCode;
	}
	public String getReceiverPcdName() {
		return receiverPcdName;
	}
	public void setReceiverPcdName(String receiverPcdName) {
		this.receiverPcdName = receiverPcdName;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getShipment() {
		return shipment;
	}
	public void setShipment(String shipment) {
		this.shipment = shipment;
	}
	public Boolean getInsureFlag() {
		return insureFlag;
	}
	public void setInsureFlag(Boolean insureFlag) {
		this.insureFlag = insureFlag;
	}
	public Double getInsurePrice() {
		return insurePrice;
	}
	public void setInsurePrice(Double insurePrice) {
		this.insurePrice = insurePrice;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
    
}
