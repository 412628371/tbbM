package com.xinguang.tubobo.merchant.api.dto;

import com.xinguang.tubobo.merchant.api.enums.EnumTaskCallbackType;

import java.io.Serializable;

/**
 * Created by xuqinghua on 2017/7/6.
 */
public class TaskCallbackDTO implements Serializable {
    EnumTaskCallbackType type;
    String jsonContent;

    public EnumTaskCallbackType getType() {
        return type;
    }

    public void setType(EnumTaskCallbackType type) {
        this.type = type;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }
}
