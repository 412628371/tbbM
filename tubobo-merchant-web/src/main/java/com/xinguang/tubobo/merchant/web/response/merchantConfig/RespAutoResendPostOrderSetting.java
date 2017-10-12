package com.xinguang.tubobo.merchant.web.response.merchantConfig;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yanxu on 2017/7/18.
 */
public class RespAutoResendPostOrderSetting implements Serializable {
    private Boolean autoPostOrder;

    public Boolean getAutoPostOrder() {
        return autoPostOrder;
    }

    public void setAutoPostOrder(Boolean autoPostOrder) {
        this.autoPostOrder = autoPostOrder;
    }
}
