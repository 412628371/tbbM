package com.xinguang.tubobo.merchant.web.request.shop;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改店铺联系方式
 */
public class ReqShopUpdatePhone implements Serializable {

    @NotBlank(message = "电话号码不能为空")
    private String newPhone;//新手机号码
    @NotBlank(message = "验证码是不能为空")
    private String verifyCode; //验证凭据

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }


    @Override
    public String toString() {
        return "ReqShopUpdatePhone{" +
                "newPhone='" + newPhone + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                '}';
    }
}
