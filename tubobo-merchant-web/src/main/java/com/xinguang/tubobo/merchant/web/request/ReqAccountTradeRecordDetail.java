package com.xinguang.tubobo.merchant.web.request;


import org.hibernate.validator.constraints.NotBlank;

public class ReqAccountTradeRecordDetail {
    @NotBlank(message = "流水id不能为空")
    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
