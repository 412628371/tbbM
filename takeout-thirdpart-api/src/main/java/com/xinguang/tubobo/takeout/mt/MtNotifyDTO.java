package com.xinguang.tubobo.takeout.mt;

import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;

import java.io.Serializable;

/**
 * 美团外卖回调DTO.
 */
public class MtNotifyDTO implements Serializable {
    private TakeoutNotifyConstant.MtNotifyType notifyType;
    private TakeoutNotifyConstant.PlatformCode platformCode;
    private String merchantId;
    private String jsonData;

    public MtNotifyDTO(){}
    public MtNotifyDTO(TakeoutNotifyConstant.PlatformCode platformCode,
                       TakeoutNotifyConstant.MtNotifyType notifyType,String merchantId,String jsonData){
        this.notifyType = notifyType;
        this.platformCode = platformCode;
        this.merchantId = merchantId;
        this.jsonData = jsonData;
    }
    public TakeoutNotifyConstant.MtNotifyType getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(TakeoutNotifyConstant.MtNotifyType notifyType) {
        this.notifyType = notifyType;
    }

    public TakeoutNotifyConstant.PlatformCode getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(TakeoutNotifyConstant.PlatformCode platformCode) {
        this.platformCode = platformCode;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
