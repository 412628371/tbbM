 package com.xinguang.tubobo.merchant.web.response;

 import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;

 import java.io.Serializable;

 public class ClientResp<T> implements Serializable{

    private String resultCode;
    private String resultDesc;
    private T resultData;

    public ClientResp(String resultCode, String resultDesc) {
    	this.resultCode = resultCode;
    	this.resultDesc = resultDesc;
    	this.resultData = null;
    }

    public ClientResp(T resultData) {
        this.resultCode = EnumRespCode.SUCCESS.getValue();
        this.resultDesc = EnumRespCode.SUCCESS.getDesc();
        this.resultData = resultData;
    }

    public String getResultCode() {
        return resultCode;
    }
    public String getResultDesc() {
        return resultDesc;
    }
    public T getResultData() {
        return resultData;
    }
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

}
