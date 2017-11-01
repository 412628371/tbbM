package com.xinguang.tubobo.merchant.web.request.shop;

import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * Created by Administrator on 2017/5/25.
 */
public class ReqUniqueLicences {

    @NotBlank(message = "营业执照号码不能为空")
    private String shopLicencesNo;

    public String getShopLicencesNo() {
        return shopLicencesNo;
    }

    public void setShopLicencesNo(String shopLicencesNo) {
        this.shopLicencesNo = shopLicencesNo;
    }
}
