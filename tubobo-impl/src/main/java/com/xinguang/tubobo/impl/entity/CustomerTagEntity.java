package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import javax.persistence.Transient;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;
/**
 * 用户标签记录
 * @author xqh
 *
 */
@Entity(noClassnameStored=true)
public class CustomerTagEntity {

	@Id
    private String id;
	@Indexed
    private String phone;
	@Indexed
    private String waybillNo;
	
	private String tags;//用户标签
	private String comment;//评价
	
	@Reference(ignoreMissing=true,lazy=true)
    private StoreEntity store; // 门店
    
	@Reference(ignoreMissing=true,lazy=true)
    private EmployeeEntity operator;//操作人
    
    private Date createDate;
    @Transient
    private Date updateDate;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWaybillNo() {
		return waybillNo;
	}
	public void setWaybillNo(String waybillNo) {
		this.waybillNo = waybillNo;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public StoreEntity getStore() {
		return store;
	}
	public void setStore(StoreEntity store) {
		this.store = store;
	}
	public EmployeeEntity getOperator() {
		return operator;
	}
	public void setOperator(EmployeeEntity operator) {
		this.operator = operator;
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
}
