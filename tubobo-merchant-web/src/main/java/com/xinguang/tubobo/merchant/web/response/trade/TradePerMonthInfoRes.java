package com.xinguang.tubobo.merchant.web.response.trade;

import java.util.Date;
import java.util.List;

/**
 * Created by yanxu on 2017/9/19.
 */
public class TradePerMonthInfoRes {
    private List<TradePerDayInfoRes> days;
    private String  month;

    public List<TradePerDayInfoRes> getDays() {
        return days;
    }

    public void setDays(List<TradePerDayInfoRes> days) {
        this.days = days;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
