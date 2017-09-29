package com.xinguang.tubobo.impl.merchant.entity;

import com.hzmux.hzcms.common.utils.IdGen;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14.
 */
@MappedSuperclass
public class BaseMerchantEntity implements Serializable{

    // 删除标记（0：正常；1：删除；2：审核；）
    public static  String FIELD_DEL_FLAG = "delFlag";
    public static  String DEL_FLAG_NORMAL = "0";
    public static  String DEL_FLAG_DELETE = "1";

    protected String id;		// 编号

    @javax.persistence.Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    protected String delFlag;
    protected Date createDate;
    protected String createBy;
    protected Date updateDate;
    protected String updateBy;

    @PrePersist
    public void prePersist(){
        this.id = IdGen.uuid();
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    @PreUpdate
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
