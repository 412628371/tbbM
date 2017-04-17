 package com.xinguang.tubobo.impl.tuboboZhushouVo;

 import com.xinguang.tubobo.api.enums.EnumRespCode;

 public class TuboboZhushouClientResp<T> {

    private String resultCode;
    private String resultDesc;
    private T resultData;

    public TuboboZhushouClientResp(String resultCode, String resultDesc) {
    	this.resultCode = resultCode;
    	this.resultDesc = resultDesc;
    	this.resultData = null;
    }

    public TuboboZhushouClientResp(EnumRespCode enumRespCode) {
    	this.resultCode = enumRespCode.getValue();
    	this.resultDesc = enumRespCode.getDesc();
    	this.resultData = null;
    }

    public TuboboZhushouClientResp(T resultData) {
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
