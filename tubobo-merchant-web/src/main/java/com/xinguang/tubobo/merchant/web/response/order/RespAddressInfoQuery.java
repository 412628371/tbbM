package com.xinguang.tubobo.merchant.web.response.order;

import com.xinguang.tubobo.api.dto.AddressDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 查询收货人地址自动补全
 * 返回数据
 */
public class RespAddressInfoQuery implements Serializable{

    private List<AddressDTO> addressList;

    private String keyword;

    public List<AddressDTO> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressDTO> addressList) {
        this.addressList = addressList;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "RespAddressInfoQuery{" +
                "addressList=" + addressList +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
