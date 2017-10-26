package com.xinguang.tubobo.impl.merchant.entity;

import org.springframework.data.annotation.CreatedDate;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/12.
 */
@Entity
@Table(name = "t_notice_entity")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PushNoticeEntity {

    private String title;
    private String noticeType;//通知类型，1-订单通知，2-公告
    private String content;
    private String userId;

    private String orderType;
    private String orderOperateType;
    private String orderNo;
    private String platformCode;
    private String originOrderViewId;

    private String identifyStatus;
    private String identifyType;
    private boolean processed;

    private String reason;
    private String recordId;
    private String cancelOrderNo;// 骑手取消的原订单号

    public boolean getProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getCancelOrderNo() {
        return cancelOrderNo;
    }

    public void setCancelOrderNo(String cancelOrderNo) {
        this.cancelOrderNo = cancelOrderNo;
    }
    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Id
    @GeneratedValue
    //@Column(name = "id", unique = true, nullable = false)
    private Long id;
    private String delFlag;
    @CreatedDate
    private Date createDate;
  //  @LastModifiedDate
    private Date updateDate;

     @PrePersist
        public void prePersist(){
            this.updateDate = new Date();
            this.createDate = this.updateDate;
        }

        @PreUpdate
        public void preUpdate(){
            this.updateDate = new Date();
        }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getOriginOrderViewId() {
        return originOrderViewId;
    }

    public void setOriginOrderViewId(String originOrderViewId) {
        this.originOrderViewId = originOrderViewId;
    }

    public String getIdentifyStatus() {
        return identifyStatus;
    }

    public void setIdentifyStatus(String identifyStatus) {
        this.identifyStatus = identifyStatus;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getOrderOperateType() {
        return orderOperateType;
    }

    public void setOrderOperateType(String orderOperateType) {
        this.orderOperateType = orderOperateType;
    }
}
