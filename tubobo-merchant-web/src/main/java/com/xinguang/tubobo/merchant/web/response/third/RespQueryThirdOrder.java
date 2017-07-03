package com.xinguang.tubobo.merchant.web.response.third;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuqinghua on 2017/7/3.
 */
public class RespQueryThirdOrder implements Serializable {
    private int pageNo;
    private int pageSize;
    private long totalSize;

    private String keyword;
    private List<RespQueryThirdOrderItem> list ;

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

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<RespQueryThirdOrderItem> getList() {
        return list;
    }

    public void setList(List<RespQueryThirdOrderItem> list) {
        this.list = list;
    }
}
