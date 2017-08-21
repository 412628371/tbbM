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
    @NotBlank(message = "操作凭据不能为空")
    private String credential; //验证凭据

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "ReqShopUpdatePhone{" +
                "newPhone='" + newPhone + '\'' +
                ", credential='" + credential + '\'' +
                '}';
    }
}
