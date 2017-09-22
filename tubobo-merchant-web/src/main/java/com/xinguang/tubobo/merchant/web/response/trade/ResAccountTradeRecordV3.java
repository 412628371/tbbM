package com.xinguang.tubobo.merchant.web.response.trade;

import java.util.List;

public class ResAccountTradeRecordV3 {
    private List<TradePerMonthInfoRes> list;

    public List<TradePerMonthInfoRes> getList() {
        return list;
    }

    public void setList(List<TradePerMonthInfoRes> list) {
        this.list = list;
    }
}
