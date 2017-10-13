package com.xinguang.tubobo.merchant.web.request;

/**
 * Created by lvhantai on 2017/10/11.
 */
public class ReqBindSetting {
    private boolean agreeBind;	    //是否同意绑定
    private long providerId;

    public boolean isAgreeBind() {
        return agreeBind;
    }

    public void setAgreeBind(boolean agreeBind) {
        this.agreeBind = agreeBind;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }
}
