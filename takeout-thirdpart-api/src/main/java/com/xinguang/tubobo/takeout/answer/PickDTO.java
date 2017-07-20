package com.xinguang.tubobo.takeout.answer;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/17.
 */
public class PickDTO implements Serializable {
    private String originOrderId;
    private String orderNo;
    private DispatcherInfoDTO dispatcherInfoDTO;

    public String getOriginOrderId() {
        return originOrderId;
    }

    public void setOriginOrderId(String originOrderId) {
        this.originOrderId = originOrderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public DispatcherInfoDTO getDispatcherInfoDTO() {
        return dispatcherInfoDTO;
    }

    public void setDispatcherInfoDTO(DispatcherInfoDTO dispatcherInfoDTO) {
        this.dispatcherInfoDTO = dispatcherInfoDTO;
    }
}
