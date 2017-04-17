package com.xinguang.tubobo.impl.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

@Entity(noClassnameStored=true)
public class ExpressCompanyEntity implements Serializable{
	
    @Id
    private String companyId;
    @Size(min=1, message="名称不能为空")
    private String companyName;
    @Size(min=1, message="联系手机不能为空")
    @Pattern(regexp="^1[0-9]{10}$", message="手机号不正确")
    private String contractPhone;

    private String regularExpression;//正则表达式
    
    private String delFlag;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    
    @Transient
    private String opened;  // 不会持久化， 只是拿来存临时业务数据  1:启用；0：禁用
    
    public ExpressCompanyEntity() {
        super();
    }
    public ExpressCompanyEntity(String companyId) {
        super();
        this.companyId = companyId;
    }
    public String getCompanyId() {
        return companyId;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getContractPhone() {
        return contractPhone;
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
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public void setContractPhone(String contractPhone) {
        this.contractPhone = contractPhone;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((companyId == null) ? 0 : companyId.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExpressCompanyEntity other = (ExpressCompanyEntity) obj;
        if (companyId == null) {
            if (other.companyId != null)
                return false;
        } else if (!companyId.equals(other.companyId))
            return false;
        return true;
    }
    public String getOpened() {
        return opened;
    }
    public void setOpened(String opened) {
        this.opened = opened;
    }
	public String getRegularExpression() {
		return regularExpression;
	}
	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}
}
