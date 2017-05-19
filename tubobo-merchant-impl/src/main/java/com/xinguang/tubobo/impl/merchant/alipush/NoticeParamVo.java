package com.xinguang.tubobo.impl.merchant.alipush;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/19.
 */
public class NoticeParamVo implements Serializable{
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
