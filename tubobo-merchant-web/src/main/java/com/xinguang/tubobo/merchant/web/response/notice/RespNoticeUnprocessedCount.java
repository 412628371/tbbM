package com.xinguang.tubobo.merchant.web.response.notice;

/**
 * Created by xuqinghua on 2017/7/24.
 */
public class RespNoticeUnprocessedCount {
   private Long businessNoticeCount;
   private Long systemNoticeCount;


    public Long getBusinessNoticeCount() {
        return businessNoticeCount;
    }

    public void setBusinessNoticeCount(Long businessNoticeCount) {
        this.businessNoticeCount = businessNoticeCount;
    }

    public Long getSystemNoticeCount() {
        return systemNoticeCount;
    }

    public void setSystemNoticeCount(Long systemNoticeCount) {
        this.systemNoticeCount = systemNoticeCount;
    }
}
