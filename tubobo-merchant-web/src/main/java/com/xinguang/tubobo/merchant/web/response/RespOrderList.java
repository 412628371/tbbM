package com.xinguang.tubobo.merchant.web.response;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public class RespOrderList {
    private int pageNo;
    private int pageSize;
    private List<RespOrderItem> list;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<RespOrderItem> getList() {
        return list;
    }

    public void setList(List<RespOrderItem> list) {
        this.list = list;
    }
}
