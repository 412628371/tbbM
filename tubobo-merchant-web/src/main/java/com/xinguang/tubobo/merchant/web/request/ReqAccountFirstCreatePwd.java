package com.xinguang.tubobo.merchant.web.request;

import org.hibernate.validator.constraints.NotBlank;

public class ReqAccountFirstCreatePwd {

    @NotBlank( message = "password 不能为空")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ReqAccountFirstCreatePwd{" +
                "password='" + password + '\'' +
                '}';
    }
}
