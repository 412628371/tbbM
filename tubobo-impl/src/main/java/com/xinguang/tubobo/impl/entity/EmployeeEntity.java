package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.hzmux.hzcms.common.utils.excel.annotation.ExcelField;

@Entity(noClassnameStored=true)
public class EmployeeEntity {

    @Id
    private String id;

    @Indexed
    @ExcelField(title="手机", sort=10)
    private String phone;
    
    private String password;
    
    @ExcelField(title="姓名", sort=20)
    private String name; 
    
    @ExcelField(title="类型", dictType="employee_type", sort=30)
    private String employeeType;
    
    @Reference(ignoreMissing=true,lazy=true)
    @ExcelField(title="所属门店", value="belongStore.storeName", sort=40)
    private StoreEntity belongStore;
    
    @Reference(ignoreMissing=true,lazy=true)
    private StoreEntity signStore;
    
    @Indexed
    private String tokenId;
    private Date tokenIdExpiredTime;

    private String delFlag;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    
    public EmployeeEntity(){
    	super();
    }

    public EmployeeEntity(String id){
    	super();
    	this.id = id;
    }
    
	public String getPhone() {
		return phone;
	}
	public String getPassword() {
		return password;
	}
	public String getName() {
		return name;
	}
	public String getEmployeeType() {
		return employeeType;
	}
	public StoreEntity getBelongStore() {
		return belongStore;
	}
	public StoreEntity getSignStore() {
		return signStore;
	}
	public String getTokenId() {
		return tokenId;
	}
	public Date getTokenIdExpiredTime() {
		return tokenIdExpiredTime;
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
	public String getDelFlag() {
		return delFlag;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public void setBelongStore(StoreEntity belongStore) {
		this.belongStore = belongStore;
	}
	public void setSignStore(StoreEntity signStore) {
		this.signStore = signStore;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public void setTokenIdExpiredTime(Date tokenIdExpiredTime) {
		this.tokenIdExpiredTime = tokenIdExpiredTime;
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
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
