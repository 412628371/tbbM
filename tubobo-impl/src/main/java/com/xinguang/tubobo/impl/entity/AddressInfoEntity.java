package com.xinguang.tubobo.impl.entity;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 地址管理实体
 */
@Entity(noClassnameStored=true)
public class AddressInfoEntity {
	
    @Id
    private String id;
    
    private String normalUserId;//用户ID

    private String  type;//寄件地址 收件地址
    private String  name;
    private String  phone;
    private String  pcdCode;//省市区代码
    private String  pcdName;//省市区中文
    private String  detailAddress;
    private Boolean isDefault;//是否默认地址

    private String delFlag;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    
	public String getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public String getPhone() {
		return phone;
	}
	public String getPcdCode() {
		return pcdCode;
	}
	public String getPcdName() {
		return pcdName;
	}
	public String getDetailAddress() {
		return detailAddress;
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
	public void setType(String type) {
		this.type = type;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setPcdCode(String pcdCode) {
		this.pcdCode = pcdCode;
	}
	public void setPcdName(String pcdName) {
		this.pcdName = pcdName;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
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
	public String getNormalUserId() {
		return normalUserId;
	}
	public void setNormalUserId(String normalUserId) {
		this.normalUserId = normalUserId;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    
}
