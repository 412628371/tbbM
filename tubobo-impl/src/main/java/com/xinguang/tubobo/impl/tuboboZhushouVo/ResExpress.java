package com.xinguang.tubobo.impl.tuboboZhushouVo;

import java.util.Date;

public class ResExpress {
	
    private String id;
    private String waybillNo; // 运单号
    private Integer waybillStatus;
    private Integer remainStatus;
    private Integer returnBackReason;
    private String expressCompanyName; // 快递公司
    private String storeName; // 门店
    private String storeAddress; // 门店地址

    private Date inTime;//入库时间
    private String inOperatorName;//入库人
    private String inOperatorPhone;//入库人
    
    private Date outTime;//出库时间
    private String outOperatorName;//出库人
    private String outOperatorPhone;//出库人

	private Date signTime;//签收时间
    private Date updateDate;//更新时间

    private String areaNum;
    
    private String pickupType;
    private String dispatchingWay;
    
    private String evaluationStar;//评分
    private String evaluationTag;//评价标签
    private String evaluationComment;//评价内容
    private Date evaluationTime;//评价时间
    
    private Double payment;//代收货款
    private Double payFreight;//代收运费
    
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
	public Integer getRemainStatus() {
		return remainStatus;
	}
	public void setRemainStatus(Integer remainStatus) {
		this.remainStatus = remainStatus;
	}
	public Integer getReturnBackReason() {
		return returnBackReason;
	}
	public void setReturnBackReason(Integer returnBackReason) {
		this.returnBackReason = returnBackReason;
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
	public Date getOutTime() {
		return outTime;
	}
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	public String getOutOperatorPhone() {
		return outOperatorPhone;
	}
	public void setOutOperatorPhone(String outOperatorPhone) {
		this.outOperatorPhone = outOperatorPhone;
	}
	public Date getSignTime() {
		return signTime;
	}
	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	public String getPickupType() {
		return pickupType;
	}
	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}
	public String getDispatchingWay() {
		return dispatchingWay;
	}
	public void setDispatchingWay(String dispatchingWay) {
		this.dispatchingWay = dispatchingWay;
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
	public Double getPayment() {
		return payment;
	}
	public void setPayment(Double payment) {
		this.payment = payment;
	}
	public Double getPayFreight() {
		return payFreight;
	}
	public void setPayFreight(Double payFreight) {
		this.payFreight = payFreight;
	}
	public String getEvaluationTag() {
		return evaluationTag;
	}
	public void setEvaluationTag(String evaluationTag) {
		this.evaluationTag = evaluationTag;
	}
	public String getExpressCompanyName() {
		return expressCompanyName;
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
	public String getInOperatorName() {
		return inOperatorName;
	}
	public void setInOperatorName(String inOperatorName) {
		this.inOperatorName = inOperatorName;
	}
	public String getOutOperatorName() {
		return outOperatorName;
	}
	public void setOutOperatorName(String outOperatorName) {
		this.outOperatorName = outOperatorName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
