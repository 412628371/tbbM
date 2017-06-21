package com.xinguang.tubobo.merchant.web.request.order.v2;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Administrator on 2017/4/14.
 */
public class ReqOrderQuery {
    @NotBlank(message = "手机号不能为空")
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ReqOrderQuery{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
