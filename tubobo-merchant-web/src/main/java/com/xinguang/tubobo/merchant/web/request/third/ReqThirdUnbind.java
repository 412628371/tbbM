package com.xinguang.tubobo.merchant.web.request.third;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xuqinghua on 2017/7/14.
 */
public class ReqThirdUnbind {
    @NotBlank(message = "平台编号不能为空")
    private String platformCode;

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }
}
