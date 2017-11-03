package com.xinguang.tubobo.merchant.web.response;

/**
 * Created by Administrator on 2017/4/17.
 */
public class RespRiderLocation {
    private Double latitude;
    //1.46 版本之前使用是该错误拼写字段 , 3个版本后 或有强制升级是去掉该字段
    private Double longtitude;
    //1.46 版本后采用此字段
    private Double longitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return "RespRiderLocation{" +
                "latitude=" + latitude +
                ", longtitude=" + longtitude +
                '}';
    }
}
