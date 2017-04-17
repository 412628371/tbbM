package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.hzmux.hzcms.common.utils.excel.annotation.ExcelField;

/**
 * 送件实体
 */
@Entity(noClassnameStored=true)
public class WayBillSEntity {
	private Boolean fromRemainFlag;//是否来自滞留件
	private String remainReason;//滞留原因
	@Id
    private String id;

	@ExcelField(title="运单号", sort=40)
	@Indexed
    private String waybillNo; // 运单号
    
	//快件状态（入库、出库、签收、滞留、退回）
	@ExcelField(title="运单状态", dictType="waybillS_status", sort=30)
    private Integer waybillStatus;
    
    //滞留件扫描次数 (区分是否滞留件)
    private Integer remainStatus;

    //退回原因 (0:默认 1:无法联系 2:超时 3:拒收 4:错分 5:改地址 6:其他)
    private Integer returnBackReason;

    @Indexed
    private String normalUserId;//用户ID
    
    @ExcelField(title="快递公司",value="expressCompany.companyName", sort=20)
    @Reference(ignoreMissing=true,lazy=true)
    private ExpressCompanyEntity expressCompany; // 快递公司

    @ExcelField(title="门店",value="belongStore.storeName", sort=10)
    @Reference(ignoreMissing=true,lazy=true)
    private StoreEntity belongStore; // 门店
    
    @ExcelField(title="入库时间", sort=110)
    private Date inTime;//入库时间
    @Reference(ignoreMissing=true,lazy=true)
    @ExcelField(title="入库人",value="inOperator.name", sort=100)
    private EmployeeEntity inOperator;//入库人
    
    @ExcelField(title="出库时间", sort=130)
    private Date outTime;//出库时间
    @Reference(ignoreMissing=true,lazy=true)
    @ExcelField(title="出库人",value="outOperator.name", sort=120)
    private EmployeeEntity outOperator;//出库人
    
    @ExcelField(title="签收时间", sort=150)
    private Date signTime;//签收时间
    @Reference(ignoreMissing=true,lazy=true)
    @ExcelField(title="签收人",value="signOperator.name", sort=140)
    private EmployeeEntity signOperator;//签收人（员工）

    //位置号、框号
    @ExcelField(title="取件码/集包号", sort=60)
    private String areaNum;
    
    //配送类型 0：自提件；  1：配送件
    private String pickupType;
    @ExcelField(title="配送方式", dictType="dispatching_way", sort=70)
    //配送方式 :0自取、1代拿、2隔天取、3白天送、4夜间送 
    private String dispatchingWay;
    
    private Boolean evaluationFlag;
    @Reference(ignoreMissing=true,lazy=true)
    private EvaluationEntity evaluation;
    
    private String senderName; // 寄件人姓名
    private String senderPhone; // 寄件人手机号
    private String senderAddress; // 寄件人地址
    private String receiverName; // 收件人姓名
    @ExcelField(title="收件人手机", sort=50)
    private String receiverPhone; // 收件人手机号
    private String receiverAddress; // 收件人地址
    
    private String paymentMethod;//付款方式
    private String shipment;//寄件物
    private Double insurePrice;//保价金额 单位：元；数字
    private Double weight;//单位：KG；数字
    @ExcelField(title="代收货款", sort=80)
    private Double payment;//代收货款
    @ExcelField(title="代收运费", sort=90)
    private Double payFreight;//代收运费
    private String remark;
    
    private String delFlag;
    @Indexed
    private Date createDate; // 录入时间
    private String createBy; // 录入人
    @ExcelField(title="更新时间", sort=160)
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
	public String getDispatchingWay() {
		return dispatchingWay;
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
	public String getPaymentMethod() {
		return paymentMethod;
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
	public Double getPayment() {
		return payment;
	}
	public Double getPayFreight() {
		return payFreight;
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
	public void setDispatchingWay(String dispatchingWay) {
		this.dispatchingWay = dispatchingWay;
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
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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
	public void setPayment(Double payment) {
		this.payment = payment;
	}
	public void setPayFreight(Double payFreight) {
		this.payFreight = payFreight;
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
	public EmployeeEntity getSignOperator() {
		return signOperator;
	}
	public void setSignOperator(EmployeeEntity signOperator) {
		this.signOperator = signOperator;
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

    public Boolean getFromRemainFlag() {
        return fromRemainFlag;
    }

    public void setFromRemainFlag(Boolean fromRemainFlag) {
        this.fromRemainFlag = fromRemainFlag;
    }

    public String getRemainReason() {
        return remainReason;
    }

    public void setRemainReason(String remainReason) {
        this.remainReason = remainReason;
    }
}
