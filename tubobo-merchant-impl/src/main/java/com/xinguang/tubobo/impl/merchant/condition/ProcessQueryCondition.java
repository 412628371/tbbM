package com.xinguang.tubobo.impl.merchant.condition;

/**
 * Created by yangxb on 2017/9/30.
 */
public class ProcessQueryCondition {

    private String userId;
    private String platformCode;
    private String keyword;
    private String queryType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
