package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/13.
 */
public class NoticeDTO implements Serializable {
    private Long id;
    private String title;
    private String noticeType;//SYSTEM("系统公告", "SYSTEM"),ORDER("订单状态", "ORDER"),AUDIT("审核状态", "AUDIT");
    private String content;
    private String summary;
    private String userId;

    private String orderType;
    private String orderOperateType;
    private String orderNo;
    private String platformCode;
    private String originOrderViewId;

    private String identifyStatus;
    private String identifyType;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderOperateType() {
        return orderOperateType;
    }

    public void setOrderOperateType(String orderOperateType) {
        this.orderOperateType = orderOperateType;
    }
}
