package com.xinguang.tubobo.merchant.web.response.marketing;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10.
 */
public class RechargeGiftPlanResp {
    List<RechargeGiftPlanItem> list;

    public List<RechargeGiftPlanItem> getList() {
        return list;
    }

    public void setList(List<RechargeGiftPlanItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "RechargeGiftPlanResp{" +
                "list=" + list +
                '}';
    }
}
