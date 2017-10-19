package com.xinguang.tubobo.impl.merchant.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseMerchantEntityId implements Serializable{

    // 删除标记（0：正常；1：删除；2：审核；）
    public static  String FIELD_DEL_FLAG = "delFlag";
    public static  String DEL_FLAG_NORMAL = "0";
    public static  String DEL_FLAG_DELETE = "1";

    protected Long id;		// 编号

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected String delFlag;
    @CreatedDate
    protected Date createDate;
    protected String createBy;
    @LastModifiedDate
    protected Date updateDate;
    protected String updateBy;

    @PrePersist
    public void prePersist(){
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    public void preUpdate(){
        this.updateDate = new Date();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
