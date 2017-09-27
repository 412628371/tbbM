package com.xinguang.tubobo.merchant.web.response.trade;

import java.util.Date;

/**
 * Created by yanxu on 2017/9/19.
 */
public class TradePerDayInfoRes {
    private String recordId;
    private String amount;
    private String type;  //对应UI订单状态，例如订单已完成
    private String currentBalance;
    private String currentFrozen;
    private String currentDeposit;
    private Date createTime;
    private String  orderNo;
    private String   tradeStatus; //v1.4.3添加，对应UI订单类型，例如骑手超时赔付

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCurrentFrozen() {
        return currentFrozen;
    }

    public void setCurrentFrozen(String currentFrozen) {
        this.currentFrozen = currentFrozen;
    }

    public String getCurrentDeposit() {
        return currentDeposit;
    }

    public void setCurrentDeposit(String currentDeposit) {
        this.currentDeposit = currentDeposit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ResAccountTradeRecord{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", currentBalance=" + currentBalance +
                ", currentFrozen=" + currentFrozen +
                ", currentDeposit=" + currentDeposit +
                ", createTime=" + createTime +
                '}';
    }
}
