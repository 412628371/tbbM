package com.xinguang.tubobo.impl.tuboboZhushouVo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class ReqAppointTask {

	@NotBlank(message = "token 不能为空")
    private String token;

	@NotBlank(message = "senderName 不能为空")
	@Size(min = 1, max = 10,message = "senderName 长度为1-10")
    private String senderName; // 寄件人姓名

	@NotBlank(message = "senderPhone 不能为空")
	@Pattern(regexp = "^1\\d{10}|((0\\d{2,3})(-)?)(\\d{7,8})((-)?(\\d{3,}))?$" ,message = "senderPhone 格式错误")
    private String senderPhone; // 寄件人手机号

	@NotBlank(message = "senderPcdCode 不能为空")
	@Pattern(regexp = "^\\d{6,8}((\\s\\d{6,8})?)((\\s\\d{6,8})?)$" ,message = "senderPcdCode 格式错误")
	private String senderPcdCode;//省市区代码

	@NotBlank(message = "senderPcdName 不能为空")
	@Pattern(regexp = "^[\\u4E00-\\u9FA5]{1,10}((\\s[\\u4E00-\\u9FA5]{1,10})?)((\\s[\\u4E00-\\u9FA5]{1,10})?)$" ,message = "senderPcdName 格式错误")
    private String senderPcdName;//省市区中文

	@NotBlank(message = "senderAddress 不能为空")
	@Size(min = 1, max = 30,message = "senderAddress 长度为1-30")
    private String senderAddress; // 寄件人详细地址

	@NotBlank(message = "receiverName 不能为空")
    @Size(min = 1, max = 10,message = "receiverName 长度为1-10")
    private String receiverName; // 收件人姓名

	@NotBlank(message = "receiverPhone 不能为空")
	@Pattern(regexp = "^1\\d{10}|((0\\d{2,3})(-)?)(\\d{7,8})((-)?(\\d{3,}))?$" ,message = "receiverPhone 格式错误")
    private String receiverPhone; // 收件人手机号

	@NotBlank(message = "receiverPcdCode 不能为空")
	@Pattern(regexp = "^\\d{6,8}((\\s\\d{6,8})?)((\\s\\d{6,8})?)$" ,message = "receiverPcdCode 格式错误")
    private String receiverPcdCode;//省市区代码

	@NotBlank(message = "receiverPcdName 不能为空")
	@Pattern(regexp = "^[\\u4E00-\\u9FA5]{1,10}((\\s[\\u4E00-\\u9FA5]{1,10})?)((\\s[\\u4E00-\\u9FA5]{1,10})?)$" ,message = "receiverPcdName 格式错误")
    private String receiverPcdName;//省市区中文

	@NotBlank(message = "receiverAddress 不能为空")
	@Size(min = 1, max = 30,message = "receiverAddress 长度为1-30")
    private String receiverAddress; // 收件人详细地址

	@NotBlank(message = "shipment 不能为空")
	@Size(max = 20,message = "shipment 长度为1-20")
    private String shipment;//寄件物

    private boolean insureFlag;//是否保价
	@Max(value = 9999 ,message = "weight 最大为9999")
	private double weight;//单位：KG；数字

    @Size(max = 60,message = "remark 长度最大为60")
    private String remark;

    @Size(max = 5,message = "source 长度最大为5")
    private String source;//来源
    
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
	public String getShipment() {
		return shipment;
	}
	public void setShipment(String shipment) {
		this.shipment = shipment;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean getInsureFlag() {
		return insureFlag;
	}
	public void setInsureFlag(boolean insureFlag) {
		this.insureFlag = insureFlag;
	}
}
