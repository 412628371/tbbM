package com.xinguang.tubobo.merchant.web.response.marketing;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/10.
 */
public class RechargeGiftPlanItem implements Serializable {
    private String price;
    private String foldback;

    public RechargeGiftPlanItem(String price,String foldback){
        this.price = price;
        this.foldback = foldback;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoldback() {
        return foldback;
    }

    public void setFoldback(String foldback) {
        this.foldback = foldback;
    }

    @Override
    public String toString() {
        return "RechargeGiftPlanItem{" +
                "price='" + price + '\'' +
                ", foldback='" + foldback + '\'' +
                '}';
    }
}
