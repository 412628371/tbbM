package com.xinguang.tubobo.api.merchant.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/15.
 */
public class GeoLocation implements Serializable{
    private String userId;
    private Double latitude;
    private Double longitude;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
