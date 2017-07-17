package com.xinguang.tubobo.takeout.answer;

import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;

import java.io.Serializable;

/**
 * 给第三方外卖平台状态同步的DTO
 */
public class ThirdAnswerDTO implements Serializable {
    private TakeoutNotifyConstant.ThirdAnswerType answerType;
    private TakeoutNotifyConstant.PlatformCode platformCode;
    private String merchantId;
    private String jsonData;
    public ThirdAnswerDTO(){}
    public ThirdAnswerDTO(TakeoutNotifyConstant.PlatformCode platformCode,
                       TakeoutNotifyConstant.ThirdAnswerType answerType,String merchantId,String jsonData){
        this.answerType = answerType;
        this.platformCode = platformCode;
        this.merchantId = merchantId;
        this.jsonData = jsonData;
    }
    public TakeoutNotifyConstant.ThirdAnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(TakeoutNotifyConstant.ThirdAnswerType answerType) {
        this.answerType = answerType;
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
