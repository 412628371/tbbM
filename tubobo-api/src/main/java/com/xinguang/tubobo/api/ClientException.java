package com.xinguang.tubobo.api;

import com.xinguang.tubobo.api.enums.EnumRespCode;

public class ClientException extends Exception {
    private String code;
    private String errorMsg;

    public ClientException(EnumRespCode resp) {
        this.code = resp.getValue();
        this.errorMsg = resp.getDesc();
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
