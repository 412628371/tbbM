package com.xinguang.tubobo.merchant.web.response.order;

import com.xinguang.tubobo.impl.merchant.common.AddressInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 查询收货人地址自动补全
 * 返回数据
 */
public class RespAddressInfoQuery implements Serializable{

    private List<AddressInfo> list;

    private String keyword;

    private long totalSize;

    public List<AddressInfo> getList() {
        return list;
    }

    public void setList(List<AddressInfo> list) {
        this.list = list;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return "RespAddressInfoQuery{" +
                "list=" + list +
                ", keyword='" + keyword + '\'' +
                ", totalSize=" + totalSize +
                '}';
    }
}
