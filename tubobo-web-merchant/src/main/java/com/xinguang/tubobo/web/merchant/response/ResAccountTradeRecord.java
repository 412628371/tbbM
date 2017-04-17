package com.xinguang.tubobo.web.merchant.response;

import java.util.Date;

public class ResAccountTradeRecord {

    private long amount;
    private String type;
    private long currentBalance;
    private long currentFrozen;
    private long currentDeposit;
    private Date createTime;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(long currentBalance) {
        this.currentBalance = currentBalance;
    }

    public long getCurrentFrozen() {
        return currentFrozen;
    }

    public void setCurrentFrozen(long currentFrozen) {
        this.currentFrozen = currentFrozen;
    }

    public long getCurrentDeposit() {
        return currentDeposit;
    }

    public void setCurrentDeposit(long currentDeposit) {
        this.currentDeposit = currentDeposit;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
