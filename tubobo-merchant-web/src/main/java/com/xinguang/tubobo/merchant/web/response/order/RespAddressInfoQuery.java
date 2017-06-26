package com.xinguang.tubobo.merchant.web.response.order;

import com.xinguang.tubobo.api.dto.AddressDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 查询收货人地址自动补全
 * 返回数据
 */
public class RespAddressInfoQuery implements Serializable{

    private List<AddressDTO> list;

    private String keyword;

    private long totalSize;

    public List<AddressDTO> getList() {
        return list;
    }

    public void setList(List<AddressDTO> list) {
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
                "keyword='" + keyword + '\'' +
                ", list=" + list +
                ", totalSize=" + totalSize +
                '}';
    }
}
