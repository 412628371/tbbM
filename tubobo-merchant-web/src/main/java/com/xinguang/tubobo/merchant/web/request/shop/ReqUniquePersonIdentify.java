package com.xinguang.tubobo.merchant.web.request.shop;

import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2017/5/25.
 */
public class ReqUniquePersonIdentify {

    @NotBlank(message = "身份证号码不能为空")
    @Pattern(regexp = MerchantConstants.PATTERN_ID_CARD ,message = "身份证号码有误")
    private String idCardNo;

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }
}
