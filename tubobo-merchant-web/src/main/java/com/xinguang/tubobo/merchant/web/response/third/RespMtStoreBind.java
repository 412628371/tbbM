package com.xinguang.tubobo.merchant.web.response.third;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/6/30.
 */
public class RespMtStoreBind implements Serializable {
    private String mtStoreBindUrl;
    private String mtStoreReleaseBindingUrl;
    private String eleStoreBindUrl;
    private String yzStoreBindUrl;

    public String getMtStoreBindUrl() {
        return mtStoreBindUrl;
    }

    public void setMtStoreBindUrl(String mtStoreBindUrl) {
        this.mtStoreBindUrl = mtStoreBindUrl;
    }

    public String getMtStoreReleaseBindingUrl() {
        return mtStoreReleaseBindingUrl;
    }

    public void setMtStoreReleaseBindingUrl(String mtStoreReleaseBindingUrl) {
        this.mtStoreReleaseBindingUrl = mtStoreReleaseBindingUrl;
    }

    public String getEleStoreBindUrl() {
        return eleStoreBindUrl;
    }

    public void setEleStoreBindUrl(String eleStoreBindUrl) {
        this.eleStoreBindUrl = eleStoreBindUrl;
    }

    public String getYzStoreBindUrl() {
        return yzStoreBindUrl;
    }

    public void setYzStoreBindUrl(String yzStoreBindUrl) {
        this.yzStoreBindUrl = yzStoreBindUrl;
    }

    @Override
    public String toString() {
        return "RespMtStoreBind{" +
                "mtStoreBindUrl='" + mtStoreBindUrl + '\'' +
                ", mtStoreReleaseBindingUrl='" + mtStoreReleaseBindingUrl + '\'' +
                ", eleStoreBindUrl='" + eleStoreBindUrl + '\'' +
                ", yzStoreBindUrl='" + yzStoreBindUrl + '\'' +
                '}';
    }
}
