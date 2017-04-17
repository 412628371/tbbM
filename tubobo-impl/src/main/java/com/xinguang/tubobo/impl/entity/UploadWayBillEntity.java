package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * 上传运单信息
 */
@Entity(noClassnameStored=true)
public class UploadWayBillEntity {

	public static final int WAYBILL_UPLOADTYPE_ZITI = 0;
	public static final int WAYBILL_UPLOADTYPE_PEISONG = 1;
	public static final int WAYBILL_UPLOADTYPE_ZHILIU = 2;

	private boolean fromRemainFlag;//是否来自滞留件
	private String remainReason;//滞留原因
	@Id
    private String id;
	
	private Date createDate;

    @Reference(ignoreMissing=true,lazy=true)
    private StoreEntity store; //门店
	
    @Reference(ignoreMissing=true,lazy=true)
    private EmployeeEntity employee;//入库人员

    private String expressCompanyId;//快递公司id

    private int uploadType;//0:自提件 1:配送件 2:滞留件
    
	private boolean smsFlag;//发送短信标记位

	private boolean delayFlag;//滞留件标记位

	private boolean sendFlag;//false:默认入库；true:直接出库配送

	private String smsModel;//发送短信模版id
	private String smsSignName;//发送短信签名
	
	private String waybillNo;//运单号
    
    private String areaNum;//位置号、框号
    
    private String receiverPhone;//收件人手机号
    
    private String payment;//代收货款
    private String payFreight;//代收运费

    private int returnBackReason;//退回原因 (0:默认 1:无法联系 2:超时 3:拒收 4:错分 5:改地址 6:其他)

	public String getId() {
		return id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public StoreEntity getStore() {
		return store;
	}


	public EmployeeEntity getEmployee() {
		return employee;
	}

	public boolean isSmsFlag() {
		return smsFlag;
	}

	public boolean isDelayFlag() {
		return delayFlag;
	}

	public boolean isSendFlag() {
		return sendFlag;
	}

	public String getSmsModel() {
		return smsModel;
	}

	public String getWaybillNo() {
		return waybillNo;
	}

	public String getAreaNum() {
		return areaNum;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public String getPayment() {
		return payment;
	}

	public String getPayFreight() {
		return payFreight;
	}

	public int getReturnBackReason() {
		return returnBackReason;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setStore(StoreEntity store) {
		this.store = store;
	}

	public void setEmployee(EmployeeEntity employee) {
		this.employee = employee;
	}

	public void setSmsFlag(boolean smsFlag) {
		this.smsFlag = smsFlag;
	}

	public void setDelayFlag(boolean delayFlag) {
		this.delayFlag = delayFlag;
	}

	public void setSendFlag(boolean sendFlag) {
		this.sendFlag = sendFlag;
	}

	public void setSmsModel(String smsModel) {
		this.smsModel = smsModel;
	}

	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}

	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public void setPayFreight(String payFreight) {
		this.payFreight = payFreight;
	}

	public void setReturnBackReason(int returnBackReason) {
		this.returnBackReason = returnBackReason;
	}

	public String getSmsSignName() {
		return smsSignName;
	}

	public void setSmsSignName(String smsSignName) {
		this.smsSignName = smsSignName;
	}

	public int getUploadType() {
		return uploadType;
	}

	public void setUploadType(int uploadType) {
		this.uploadType = uploadType;
	}

	public String getExpressCompanyId() {
		return expressCompanyId;
	}

	public void setExpressCompanyId(String expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}

	public boolean getFromRemainFlag() {
		return fromRemainFlag;
	}

	public void setFromRemainFlag(boolean fromRemainFlag) {
		this.fromRemainFlag = fromRemainFlag;
	}

	public String getRemainReason() {
		return remainReason;
	}

	public void setRemainReason(String remainReason) {
		this.remainReason = remainReason;
	}
}
