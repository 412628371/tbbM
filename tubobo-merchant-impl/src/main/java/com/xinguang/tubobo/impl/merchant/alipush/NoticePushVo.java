package com.xinguang.tubobo.impl.merchant.alipush;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NoticePushVo implements Serializable{
    private String type;
    private String noticeType;
    private String userId;
    private NoticeParamVo params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NoticeParamVo getParams() {
        return params;
    }

    public void setParams(NoticeParamVo params) {
        this.params = params;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
}
