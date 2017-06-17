package com.xinguang.tubobo.merchant.web.common.info;

import java.io.Serializable;

/**
 * 车辆类型信息.
 */
public class CarInfo implements Serializable {
    private String carType;
    private String carTypeName;

    public CarInfo(String carType,String carTypeName){
        this.carType = carType;
        this.carTypeName = carTypeName;
    }
    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }
}
