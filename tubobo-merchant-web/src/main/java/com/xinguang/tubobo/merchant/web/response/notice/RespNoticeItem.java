package com.xinguang.tubobo.merchant.web.response.notice;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/18.
 */
public class RespNoticeItem implements Serializable {
    private String title;
    private String noticeType;//通知类型，1-订单通知，2-公告
    private String content;

    private String orderType;
    private String orderNo;
    private String platformCode;
    private String originOrderViewId;

    private boolean processed;
    protected Date createTime;
    protected Long id;
    private  String recordId;  //罚款交易编号Id

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
