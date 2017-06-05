package com.xinguang.tubobo.merchant.web.response.order;

import java.io.Serializable;

/**
 * 开通的城市.
 */
public class RespOrderOpenedItem implements Serializable{
    private String code;
    private String name;
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
