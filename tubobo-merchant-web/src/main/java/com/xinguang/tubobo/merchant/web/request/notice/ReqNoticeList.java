package com.xinguang.tubobo.merchant.web.request.notice;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/18.
 */
public class ReqNoticeList implements Serializable {
    private Integer pageSize;
    private Integer pageNo;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
