package com.xinguang.tubobo.impl.merchant.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/1.
 */
@Entity
@Table(name = "t_third_mt_order")
@DynamicInsert
@DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ThirdMtOrderEntity implements Serializable {

    private String userId;
    private String originOrderId;
    private String originOrderViewId;
    private String receiverAddressDetail;
    private String receiverName;
    private String receiverPhone;
    private Double receiverLatitude;
    private Double receiverLongitude;

    private boolean processed;
    private Date originCreateTime;
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;
    protected String delFlag;
    protected Date createDate;
    protected Date updateDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(String originOrderId) {
        this.originOrderId = originOrderId;
    }

    public String getOriginOrderViewId() {
        return originOrderViewId;
    }

    public void setOriginOrderViewId(String originOrderViewId) {
        this.originOrderViewId = originOrderViewId;
    }

    public String getReceiverAddressDetail() {
        return receiverAddressDetail;
    }

    public void setReceiverAddressDetail(String receiverAddressDetail) {
        this.receiverAddressDetail = receiverAddressDetail;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Double getReceiverLatitude() {
        return receiverLatitude;
    }

    public void setReceiverLatitude(Double receiverLatitude) {
        this.receiverLatitude = receiverLatitude;
    }

    public Double getReceiverLongitude() {
        return receiverLongitude;
    }

    public void setReceiverLongitude(Double receiverLongitude) {
        this.receiverLongitude = receiverLongitude;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Date getOriginCreateTime() {
        return originCreateTime;
    }

    public void setOriginCreateTime(Date originCreateTime) {
        this.originCreateTime = originCreateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
