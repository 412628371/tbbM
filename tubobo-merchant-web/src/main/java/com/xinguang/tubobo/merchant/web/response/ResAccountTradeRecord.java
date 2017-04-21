package com.xinguang.tubobo.merchant.web.response;

import java.util.Date;

public class ResAccountTradeRecord {

    private String recordId;
    private String amount;
    private String type;
    private String currentBalance;
    private String currentFrozen;
    private String currentDeposit;
    private Date createTime;

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
