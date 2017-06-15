package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.api.util.ErrorMsgHelper;
import org.apache.commons.lang3.StringUtils;

public class MerchantClientException extends Exception {
    private String code;
    private String errorMsg;

    public MerchantClientException(EnumRespCode resp) {
        this.code = resp.getValue();
        this.errorMsg = ErrorMsgHelper.findByCode(code);
        if (StringUtils.isBlank(errorMsg)){
            this.errorMsg = resp.getDesc();
        }
    }

    public MerchantClientException(EnumRespCode resp,String placeHolder) {
        this.code = resp.getValue();
        this.errorMsg = String.format(resp.getDesc(),placeHolder);
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
