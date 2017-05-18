package com.xinguang.tubobo.merchant.web.request;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/5/17.
 */
public class ReqAccountResetPwd {
    @NotBlank(message = "操作凭据不能为空")
    private String credential;
    @NotBlank(message = "请输入支付密码")
    private String payPassword;

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    @Override
    public String toString() {
        return "ReqAccountResetPwd{" +
                "credential='" + credential + '\'' +
                '}';
    }
}
