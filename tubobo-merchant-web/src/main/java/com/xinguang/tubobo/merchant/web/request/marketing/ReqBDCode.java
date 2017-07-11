package com.xinguang.tubobo.merchant.web.request.marketing;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * Created by lvhantai on 2017/6/21.
 */
public class ReqBDCode implements Serializable{
    @NotBlank(message = "邀请码不能为空")
    private String bdCode;

    public String getBdCode() {
        return bdCode;
    }

    public void setBdCode(String bdCode) {
        this.bdCode = bdCode;
    }
}
