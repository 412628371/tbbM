package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * 司机/骑手信息.
 */
public class DriverInfo implements Serializable{

    private String carNo;
    private String carType;
    private String name;
    private String telephone;

    private Double longitude;
    private Double latitude;

    public DriverInfo(String name,String phone,String carNo,String carType,Double latitude,Double longitude){
        this.name = name;
        this.telephone = phone;
        this.carNo = carNo;
        this.carType = carType;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

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
}
