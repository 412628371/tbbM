package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.util.Date;

/**
 * 收件任务
 */
public class ResAppointTask {

	private String id;

    private String waybillNo; // 运单号
	private Integer waybillStatus;

    private String taskStatus;
    
    private String expressCompanyName; // 快递公司

    private String storeName; // 门店
    private String storeAddress; // 门店地址
    
    private Date inTime;//入库时间
    private String inOperatorName;//入库人员
    private String inOperatorPhone;//快递员
    
    private String evaluationStar;//评分
    private String evaluationTag;//评价标签
    private String evaluationComment;//评价内容
    private Date evaluationTime;//评价时间
    
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
    
    private String shipment;//寄件物
    private Boolean insureFlag;//是否保价
    private Double weight;//单位：KG；数字
    private String remark;
    private String source;//来源
    
    private String paymentMethod;//付款方式 线上/线下
    private Double payFreight;//运费
    private String payStatus;//付款状态
    private Date payTime; // 付款时间
    private String payType; // 支付方式 wxpay/alipay
    
    private Date createDate; // 录入时间
    
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
	public Date getInTime() {
		return inTime;
	}
	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}
	public String getInOperatorPhone() {
		return inOperatorPhone;
	}
	public void setInOperatorPhone(String inOperatorPhone) {
		this.inOperatorPhone = inOperatorPhone;
	}
	public String getEvaluationStar() {
		return evaluationStar;
	}
	public void setEvaluationStar(String evaluationStar) {
		this.evaluationStar = evaluationStar;
	}
	public String getEvaluationComment() {
		return evaluationComment;
	}
	public void setEvaluationComment(String evaluationComment) {
		this.evaluationComment = evaluationComment;
	}
	public Date getEvaluationTime() {
		return evaluationTime;
	}
	public void setEvaluationTime(Date evaluationTime) {
		this.evaluationTime = evaluationTime;
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
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getShipment() {
		return shipment;
	}
	public void setShipment(String shipment) {
		this.shipment = shipment;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getPayFreight() {
		return payFreight;
	}
	public void setPayFreight(Double payFreight) {
		this.payFreight = payFreight;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getEvaluationTag() {
		return evaluationTag;
	}
	public void setEvaluationTag(String evaluationTag) {
		this.evaluationTag = evaluationTag;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public Boolean getInsureFlag() {
		return insureFlag;
	}
	public void setInsureFlag(Boolean insureFlag) {
		this.insureFlag = insureFlag;
	}
	public String getExpressCompanyName() {
		return expressCompanyName;
	}
	public String getInOperatorName() {
		return inOperatorName;
	}
	public void setInOperatorName(String inOperatorName) {
		this.inOperatorName = inOperatorName;
	}
	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
    
}
