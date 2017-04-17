package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity(noClassnameStored=true)
public class LoginLogEntity {
	
	@Id
	private String tokenId;

    private String employeeId;
    private String phone;
    private String employeeType;
    
    private String signStore;
    private String storeName;
    private Date loginTime;
    
	public String getTokenId() {
		return tokenId;
	}
	public String getSignStore() {
		return signStore;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public String getEmployeeType() {
		return employeeType;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public void setSignStore(String signStore) {
		this.signStore = signStore;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
