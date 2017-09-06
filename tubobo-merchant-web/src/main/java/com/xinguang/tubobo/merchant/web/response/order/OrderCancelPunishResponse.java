package com.xinguang.tubobo.merchant.web.response.order;

import java.io.Serializable;

/**
 * Created by lvhantai on 2017/9/5.
 */
public class OrderCancelPunishResponse implements Serializable{
    private Double cancelPrice;

    public Double getCancelPrice() {
        return cancelPrice;
    }

    public void setCancelPrice(Double cancelPrice) {
        this.cancelPrice = cancelPrice;
    }
}
