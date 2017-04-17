package com.xinguang.tubobo.impl.tuboboZhushouVo;

public class ResWaybillS {
	
    private String waybillNo;
    
    private Integer waybillStatus;
    
    private Integer remainStatus;

    private String receiverPhone;
    
    private String areaNum;
    
    private String pickupType;
    
    //add by xqh for 配送件的滞留处理
    private Double payFreight;
    private Double payment;
    private String expressCompanyId;
    private String expressCompanyName;

	public String getWaybillNo() {
		return waybillNo;
	}

	public Integer getWaybillStatus() {
		return waybillStatus;
	}

	public Integer getRemainStatus() {
		return remainStatus;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public String getAreaNum() {
		return areaNum;
	}

	public String getPickupType() {
		return pickupType;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public void setWaybillStatus(Integer waybillStatus) {
		this.waybillStatus = waybillStatus;
	}

	public void setRemainStatus(Integer remainStatus) {
		this.remainStatus = remainStatus;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}

	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}

	public Double getPayFreight() {
		return payFreight;
	}

	public void setPayFreight(Double payFreight) {
		this.payFreight = payFreight;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public String getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(String expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}

	public String getExpressCompanyName() {
		return expressCompanyName;
	}

	public void setExpressCompanyName(String expressCompanyName) {
		this.expressCompanyName = expressCompanyName;
	}
}
