package com.xinguang.tubobo.merchant.web.response;

/**
 * Created by lvhantai on 2017/10/11.
 */
public class RespShopBindQuery {
    private String bindStatus;	    //授权状态
    private String operatorName;	//驿站联系人
    private String operatorPhone;	//驿站联系方式
    private long providerId;	    //驿站ID
    private String providerName;	//驿站服务商名字

    public String getBindStatus() {
        return bindStatus;
    }

    public void setBindStatus(String bindStatus) {
        this.bindStatus = bindStatus;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorPhone() {
        return operatorPhone;
    }

    public void setOperatorPhone(String operatorPhone) {
        this.operatorPhone = operatorPhone;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
