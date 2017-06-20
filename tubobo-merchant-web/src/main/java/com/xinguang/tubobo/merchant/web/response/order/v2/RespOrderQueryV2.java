package com.xinguang.tubobo.merchant.web.response.order.v2;

import com.xinguang.tubobo.api.dto.AddressDTO;

import java.io.Serializable;
import java.util.List;

/**
 * 订单查询v2.0.
 */
public class RespOrderQueryV2 implements Serializable{

    private List<AddressDTO> addressList;

    public List<AddressDTO> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressDTO> addressList) {
        this.addressList = addressList;
    }

    @Override
    public String toString() {
        return "RespOrderQueryV2{" +
                "addressList=" + addressList +
                '}';
    }
}
