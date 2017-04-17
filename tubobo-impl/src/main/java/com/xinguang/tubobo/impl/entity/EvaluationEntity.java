package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import javax.persistence.Transient;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 用户评价
 */
@Entity(noClassnameStored=true)
public class EvaluationEntity {
	
	@Id
    private String id;

	private String type;// 收件评价、寄件评价
	private String dataId;
	private String waybillNo;

    private String userId;//用户ID
    private String name;
    private String phone;

    private String evaluationStar;//评分
    private String evaluationTag;//评价标签
    private String evaluationComment;//评价内容
    private Date evaluationTime;//评价时间

    @Transient
    private Date createDate;
    @Transient
    private Date updateDate;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEvaluationStar() {
		return evaluationStar;
	}
	public void setEvaluationStar(String evaluationStar) {
		this.evaluationStar = evaluationStar;
	}
	public String getEvaluationTag() {
		return evaluationTag;
	}
	public void setEvaluationTag(String evaluationTag) {
		this.evaluationTag = evaluationTag;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	
}
