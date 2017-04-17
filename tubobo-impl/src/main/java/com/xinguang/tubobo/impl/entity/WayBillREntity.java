package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.hzmux.hzcms.common.utils.excel.annotation.ExcelField;

/**
 * 收件运单
 */
@Entity(noClassnameStored=true)
public class WayBillREntity {
	
	@Id
    private String id;

	@ExcelField(title="运单号", sort=40)
	@Indexed
    private String waybillNo; // 运单号
	@ExcelField(title="运单状态",dictType="waybillR_status", sort=30)
	private Integer waybillStatus;

    private String taskStatus;
    
    @Indexed
    private String normalUserId;//用户ID
    
    @ExcelField(title="快递公司",value="expressCompany.companyName", sort=20)
    @Reference(ignoreMissing=true,lazy=true)
    private ExpressCompanyEntity expressCompany; // 快递公司

    @ExcelField(title="门店", value="belongStore.storeName", sort=10)
    @Reference(ignoreMissing=true,lazy=true)
    private StoreEntity belongStore; // 门店
    
    @ExcelField(title="入库时间", sort=100)
    private Date inTime;//入库时间
    @ExcelField(title="入库人", value="inOperator.name", sort=105)
    @Reference(ignoreMissing=true,lazy=true)
    private EmployeeEntity inOperator;//入库人员
    
    @ExcelField(title="出库时间", sort=110)
    private Date outTime;//出库时间
    @Reference(ignoreMissing=true,lazy=true)
    private EmployeeEntity outOperator;//出库人员
    
    private Boolean evaluationFlag;//是否评价
    @Reference(ignoreMissing=true,lazy=true)
    private EvaluationEntity evaluation;

	@ExcelField(title="寄件人", sort=49)
    private String senderName; // 寄件人姓名
    @ExcelField(title="寄件人手机", sort=50)
    private String senderPhone; // 寄件人手机号
    private String senderPcdCode;//省市区代码
	@ExcelField(title="寄件省市区", sort=51)
    private String senderPcdName;//省市区中文
	@ExcelField(title="寄件详细地址", sort=52)
    private String senderAddress; // 寄件人详细地址
	@ExcelField(title="收件人", sort=53)
    private String receiverName; // 收件人姓名
	@ExcelField(title="收件人手机号", sort=54)
    private String receiverPhone; // 收件人手机号
    private String receiverPcdCode;//省市区代码
    @ExcelField(title="收件省市区", sort=60)
    private String receiverPcdName;//省市区中文
    @ExcelField(title="收件详细地址", sort=65)
    private String receiverAddress; // 收件人详细地址
    
    private String customerType;//寄件客户类型
    
    private String shipment;//寄件物
    @ExcelField(title="是否保价", sort=71)
    private Boolean insureFlag;//是否保价
    private Double insurePrice;//保价金额 单位：元；数字
    @ExcelField(title="重量", sort=80)
    private Double weight;//单位：KG；数字
    private String size;//物品大小
    private String remark;
    private String source;//来源

    private String payMethod;//付款方式 线上/线下
    @ExcelField(title="运费", sort=70)
    private Double payFreight;//运费
    private String payStatus;//付款状态
    private Date payTime; // 付款时间
    private String payType; // 支付方式 wxpay/alipay
    
    private String delFlag;
    @ExcelField(title="录入时间", sort=90)
    @Indexed
    private Date createDate; // 录入时间
    private String createBy; // 录入人 
    private Date updateDate; // 更新时间
    private String updateBy; // 更新人
    
	public String getId() {
		return id;
	}
	public String getWaybillNo() {
		return waybillNo;
	}
	public String getNormalUserId() {
		return normalUserId;
	}
	public ExpressCompanyEntity getExpressCompany() {
		return expressCompany;
	}
	public StoreEntity getBelongStore() {
		return belongStore;
	}
	public Date getInTime() {
		return inTime;
	}
	public EmployeeEntity getInOperator() {
		return inOperator;
	}
	public Date getOutTime() {
		return outTime;
	}
	public EmployeeEntity getOutOperator() {
		return outOperator;
	}
	public String getSenderName() {
		return senderName;
	}
	public String getSenderPhone() {
		return senderPhone;
	}
	public String getSenderAddress() {
		return senderAddress;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public String getShipment() {
		return shipment;
	}
	public Double getInsurePrice() {
		return insurePrice;
	}
	public Double getWeight() {
		return weight;
	}
	public Double getPayFreight() {
		return payFreight;
	}
	public String getSize() {
		return size;
	}
	public String getRemark() {
		return remark;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public String getCreateBy() {
		return createBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public void setNormalUserId(String normalUserId) {
		this.normalUserId = normalUserId;
	}
	public void setExpressCompany(ExpressCompanyEntity expressCompany) {
		this.expressCompany = expressCompany;
	}
	public void setBelongStore(StoreEntity belongStore) {
		this.belongStore = belongStore;
	}
	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}
	public void setInOperator(EmployeeEntity inOperator) {
		this.inOperator = inOperator;
	}
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}
	public void setOutOperator(EmployeeEntity outOperator) {
		this.outOperator = outOperator;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public void setShipment(String shipment) {
		this.shipment = shipment;
	}
	public void setInsurePrice(Double insurePrice) {
		this.insurePrice = insurePrice;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public void setPayFreight(Double payFreight) {
		this.payFreight = payFreight;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Integer getWaybillStatus() {
		return waybillStatus;
	}
	public void setWaybillStatus(Integer waybillStatus) {
		this.waybillStatus = waybillStatus;
	}
	public String getSenderPcdCode() {
		return senderPcdCode;
	}
	public String getSenderPcdName() {
		return senderPcdName;
	}
	public String getReceiverPcdCode() {
		return receiverPcdCode;
	}
	public String getReceiverPcdName() {
		return receiverPcdName;
	}
	public void setSenderPcdCode(String senderPcdCode) {
		this.senderPcdCode = senderPcdCode;
	}
	public void setSenderPcdName(String senderPcdName) {
		this.senderPcdName = senderPcdName;
	}
	public void setReceiverPcdCode(String receiverPcdCode) {
		this.receiverPcdCode = receiverPcdCode;
	}
	public void setReceiverPcdName(String receiverPcdName) {
		this.receiverPcdName = receiverPcdName;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
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
	public Boolean getInsureFlag() {
		return insureFlag;
	}
	public void setInsureFlag(Boolean insureFlag) {
		this.insureFlag = insureFlag;
	}
	public EvaluationEntity getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(EvaluationEntity evaluation) {
		this.evaluation = evaluation;
	}
	public Boolean getEvaluationFlag() {
		return evaluationFlag;
	}
	public void setEvaluationFlag(Boolean evaluationFlag) {
		this.evaluationFlag = evaluationFlag;
	}
	public String getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
}
