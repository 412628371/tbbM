package com.xinguang.tubobo.merchant.web.request;

/**
 * Created by Administrator on 2017/5/16.
 */
public class ReqPayPwdFree {
    private String payPassword;
    private Boolean enablePwdFree;

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public Boolean getEnablePwdFree() {
        return enablePwdFree;
    }

    public void setEnablePwdFree(Boolean enablePwdFree) {
        this.enablePwdFree = enablePwdFree;
    }
}
