package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.util.List;

public class ReqLanshouBatch {

	private String tokenId;
	private String customerType;//寄件客户类型
    private String senderName; // 寄件人姓名
    private String senderPhone; // 寄件人手机号
    private String senderPcdCode;//省市区代码
    private String senderPcdName;//省市区中文
    private String senderAddress; // 寄件人详细地址
	private List<ReqLanshouBatchItem> waybills;
	
	public static class ReqLanshouBatchItem {

	    private String receiverName; // 收件人姓名
	    private String receiverPhone; // 收件人手机号
	    private String receiverPcdCode;//省市区代码
	    private String receiverPcdName;//省市区中文
	    private String receiverAddress; // 收件人详细地址
	    
	    private String expressCompanyId;
	    private String waybillNo;
	    private double payFreight;
	    private double weight;
		private double insurePrice;
		private boolean insureFlag;
		
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
		public String getExpressCompanyId() {
			return expressCompanyId;
		}
		public void setExpressCompanyId(String expressCompanyId) {
			this.expressCompanyId = expressCompanyId;
		}
		public String getWaybillNo() {
			return waybillNo;
		}
		public void setWaybillNo(String waybillNo) {
			this.waybillNo = waybillNo;
		}
		public double getPayFreight() {
			return payFreight;
		}
		public void setPayFreight(double payFreight) {
			this.payFreight = payFreight;
		}
		public double getWeight() {
			return weight;
		}
		public void setWeight(double weight) {
			this.weight = weight;
		}
		public double getInsurePrice() {
			return insurePrice;
		}
		public void setInsurePrice(double insurePrice) {
			this.insurePrice = insurePrice;
		}
		public boolean getInsureFlag() {
			return insureFlag;
		}
		public void setInsureFlag(boolean insureFlag) {
			this.insureFlag = insureFlag;
		}
	}
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
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
	public List<ReqLanshouBatchItem> getWaybills() {
		return waybills;
	}
	public void setWaybills(List<ReqLanshouBatchItem> waybills) {
		this.waybills = waybills;
	}
	
}
