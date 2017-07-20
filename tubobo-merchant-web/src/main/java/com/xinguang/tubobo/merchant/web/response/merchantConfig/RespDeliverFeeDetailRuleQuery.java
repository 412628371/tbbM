package com.xinguang.tubobo.merchant.web.response.merchantConfig;

import java.io.Serializable;

/**
 * Created by yanxu on 2017/7/18.
 */
public class RespDeliverFeeDetailRuleQuery implements Serializable {
    private Double beginDistance;
    private Double endDistance;
    private Double price;

    public Double getBeginDistance() {
        return beginDistance;
    }

    public void setBeginDistance(Double beginDistance) {
        this.beginDistance = beginDistance;
    }

    public Double getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(Double endDistance) {
        this.endDistance = endDistance;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
