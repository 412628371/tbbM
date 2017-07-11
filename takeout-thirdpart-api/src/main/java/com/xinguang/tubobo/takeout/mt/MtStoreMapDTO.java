package com.xinguang.tubobo.takeout.mt;

import java.io.Serializable;

/**
 * 美团店铺映射回调DTO.
 * @author xqh
 */
public class MtStoreMapDTO implements Serializable {
    private String appAuthToken;
    private String timestamp;

    public MtStoreMapDTO(){
    }
    public MtStoreMapDTO(String appAuthToken,String timestamp){
        this.appAuthToken = appAuthToken;
        this.timestamp = timestamp;
    }
    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
