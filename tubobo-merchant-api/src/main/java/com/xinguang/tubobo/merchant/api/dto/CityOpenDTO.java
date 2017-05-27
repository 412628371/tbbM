package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/26.
 */
public class CityOpenDTO implements Serializable {
    private String code;
    private String name;
    private String openType;

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

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    @Override
    public String toString() {
        return "CityOpenDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", openType='" + openType + '\'' +
                '}';
    }
}
