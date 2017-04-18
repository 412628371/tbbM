package com.xinguang.tubobo.merchant.web.request;

import javax.validation.constraints.Min;

public class ReqAccountWithdraw {

    @Min(value = 1 ,message = "amount 最小为1")
    private long amount;

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
