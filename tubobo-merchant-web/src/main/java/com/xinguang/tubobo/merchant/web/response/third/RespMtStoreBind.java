package com.xinguang.tubobo.merchant.web.response.third;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/6/30.
 */
public class RespMtStoreBind implements Serializable {
    private String mtStoreBindUrl;
    private String mtStoreReleaseBindingUrl;

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

    @Override
    public String toString() {
        return "RespMtStoreBind{" +
                "mtStoreBindUrl='" + mtStoreBindUrl + '\'' +
                ", mtStoreReleaseBindingUrl='" + mtStoreReleaseBindingUrl + '\'' +
                '}';
    }
}
