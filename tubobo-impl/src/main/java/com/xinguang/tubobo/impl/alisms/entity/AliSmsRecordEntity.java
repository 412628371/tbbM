package com.xinguang.tubobo.impl.alisms.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

/**
 * 阿里短信发送记录
 */
@Entity(noClassnameStored=true)
public class AliSmsRecordEntity {
	
	@Id
	private String id;
	
	private String appId;//应用id

	private String aliSmsId;//阿里短信模版id

	private String aliSignName;//阿里短信模版签名
	
	private String smsType;//短信类型

	private String storeId;//所属门店
	private String operator;//操作人
	
	private boolean immediate;//是否立即发送标记位
	
	private String phone;//接收号码 
	private String jsonParams;//json参数

	/** 状态 0:待发送;1:等待回执;2:发送失败;3:发送成功;10:异常状态; **/
	private String smsStatus;

	/** 成功时 有效返回信息 **/
	@Indexed
	private String bizId;//返回结果（短信发送流水）
	private String bizResultCode;//返回码
	private boolean bizSuccess;//true表示成功，false表示失败
	private String bizMsg;//返回信息描述

	/** 失败时 有效返回信息 **/
	private String taobaoResponseErrorCode;
	private String taobaoResponseMsg;
	private String taobaoResponseSubCode;
	private String taobaoResponseSubMsg;
	
	private String sendTime;//发送时间
	private String receiverTime;//接收时间
	
	@Indexed
	private Date createDate;//录入时间
	private Date updateDate;//更新时间
	
	public String getId() {
		return id;
	}
	public String getAppId() {
		return appId;
	}
	public String getPhone() {
		return phone;
	}
	public String getSmsStatus() {
		return smsStatus;
	}
	public String getOperator() {
		return operator;
	}
	public String getSendTime() {
		return sendTime;
	}
	public String getReceiverTime() {
		return receiverTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public void setReceiverTime(String receiverTime) {
		this.receiverTime = receiverTime;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getAliSmsId() {
		return aliSmsId;
	}
	public String getAliSignName() {
		return aliSignName;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setAliSmsId(String aliSmsId) {
		this.aliSmsId = aliSmsId;
	}
	public void setAliSignName(String aliSignName) {
		this.aliSignName = aliSignName;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getJsonParams() {
		return jsonParams;
	}
	public void setJsonParams(String jsonParams) {
		this.jsonParams = jsonParams;
	}
	public boolean isImmediate() {
		return immediate;
	}
	public void setImmediate(boolean immediate) {
		this.immediate = immediate;
	}
	public boolean isBizSuccess() {
		return bizSuccess;
	}
	public String getBizMsg() {
		return bizMsg;
	}
	public String getTaobaoResponseErrorCode() {
		return taobaoResponseErrorCode;
	}
	public String getTaobaoResponseMsg() {
		return taobaoResponseMsg;
	}
	public String getTaobaoResponseSubCode() {
		return taobaoResponseSubCode;
	}
	public String getTaobaoResponseSubMsg() {
		return taobaoResponseSubMsg;
	}
	public void setBizSuccess(boolean bizSuccess) {
		this.bizSuccess = bizSuccess;
	}
	public void setBizMsg(String bizMsg) {
		this.bizMsg = bizMsg;
	}
	public void setTaobaoResponseErrorCode(String taobaoResponseErrorCode) {
		this.taobaoResponseErrorCode = taobaoResponseErrorCode;
	}
	public void setTaobaoResponseMsg(String taobaoResponseMsg) {
		this.taobaoResponseMsg = taobaoResponseMsg;
	}
	public void setTaobaoResponseSubCode(String taobaoResponseSubCode) {
		this.taobaoResponseSubCode = taobaoResponseSubCode;
	}
	public void setTaobaoResponseSubMsg(String taobaoResponseSubMsg) {
		this.taobaoResponseSubMsg = taobaoResponseSubMsg;
	}
	public String getBizResultCode() {
		return bizResultCode;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizResultCode(String bizResultCode) {
		this.bizResultCode = bizResultCode;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	@Override
	public String toString() {
		return "AliSmsRecordEntity [aliSmsId=" + aliSmsId + ", aliSignName="
				+ aliSignName + ", immediate=" + immediate + ", phone=" + phone
				+ ", jsonParams=" + jsonParams + ", smsStatus=" + smsStatus
				+ ", bizResultCode=" + bizResultCode + ", bizId=" + bizId
				+ ", bizSuccess=" + bizSuccess + ", bizMsg=" + bizMsg
				+ ", taobaoResponseErrorCode=" + taobaoResponseErrorCode
				+ ", taobaoResponseMsg=" + taobaoResponseMsg
				+ ", taobaoResponseSubCode=" + taobaoResponseSubCode
				+ ", taobaoResponseSubMsg=" + taobaoResponseSubMsg
				+ ", createDate=" + createDate + ", updateDate=" + updateDate
				+ "]";
	}
	
}
