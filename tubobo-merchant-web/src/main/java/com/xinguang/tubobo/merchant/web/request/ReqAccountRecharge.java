package com.xinguang.tubobo.merchant.web.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ReqAccountRecharge {
//    @NotBlank(message = "充值方式不能为空")
    private String chargeType;
    private String clientIp;
    @DecimalMin(value = "0.01",message = "金额不能小于一分")
    private Double amount;

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
