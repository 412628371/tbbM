package com.xinguang.tubobo.merchant.web.response.merchantConfig;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yanxu on 2017/7/18.
 */
public class RespDeliverFeeRuleQuery implements Serializable {
    private Double initDistance;
    private Double initPrice;
    private List<RespDeliverFeeDetailRuleQuery> list;

    public Double getInitDistance() {
        return initDistance;
    }

    public void setInitDistance(Double initDistance) {
        this.initDistance = initDistance;
    }

    public Double getInitPrice() {
        return initPrice;
    }

    public void setInitPrice(Double initPrice) {
        this.initPrice = initPrice;
    }

    public List<RespDeliverFeeDetailRuleQuery> getList() {
        return list;
    }

    public void setList(List<RespDeliverFeeDetailRuleQuery> list) {
        this.list = list;
    }
}
