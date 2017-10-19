package com.xinguang.tubobo.merchant.web.request.shop;


import java.io.Serializable;

/**
 * Created by yx on 2017/10/12.
 */
public class ReqAutoPostOrderOperate implements Serializable {
    private Boolean autoPostOrder;

    public Boolean getAutoPostOrder() {
        return autoPostOrder;
    }

    public void setAutoPostOrder(Boolean autoPostOrder) {
        this.autoPostOrder = autoPostOrder;
    }
}
