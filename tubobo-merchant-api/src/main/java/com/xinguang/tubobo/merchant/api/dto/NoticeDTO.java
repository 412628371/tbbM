package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/13.
 */
public class NoticeDTO implements Serializable {
    private String title;
    private String content;
    private String userId;
    private String orderType;

    private Integer noticeType;//通知类型，1-订单通知，2-公告

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

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }
}
