package com.xinguang.tubobo.merchant.api.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/27.
 */
public class CarTypeDTO implements Serializable {
    private String iconUrl;
    private String carType;//小型面包：small-minibus;中型面包: medium-minibus;小型厢货：small-van;中型厢货：medium-van
    private String name;
    private Double startPrice;
    private Double startDistance;
    private Double beyondPrice;
    private Double maxLoad;
    private Double volumeLong;
    private Double volumeWidth;
    private Double volumeHeight;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public Double getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Double startPrice) {
        this.startPrice = startPrice;
    }

    public Double getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(Double startDistance) {
        this.startDistance = startDistance;
    }

    public Double getBeyondPrice() {
        return beyondPrice;
    }

    public void setBeyondPrice(Double beyondPrice) {
        this.beyondPrice = beyondPrice;
    }

    public Double getMaxLoad() {
        return maxLoad;
    }

    public void setMaxLoad(Double maxLoad) {
        this.maxLoad = maxLoad;
    }

    public Double getVolumeLong() {
        return volumeLong;
    }

    public void setVolumeLong(Double volumeLong) {
        this.volumeLong = volumeLong;
    }

    public Double getVolumeWidth() {
        return volumeWidth;
    }

    public void setVolumeWidth(Double volumeWidth) {
        this.volumeWidth = volumeWidth;
    }

    public Double getVolumeHeight() {
        return volumeHeight;
    }

    public void setVolumeHeight(Double volumeHeight) {
        this.volumeHeight = volumeHeight;
    }

    @Override
    public String toString() {
        return "CarTypeDTO{" +
                "iconUrl='" + iconUrl + '\'' +
                ", carType='" + carType + '\'' +
                ", name='" + name + '\'' +
                ", startPrice=" + startPrice +
                ", startDistance=" + startDistance +
                ", beyondPrice=" + beyondPrice +
                ", maxLoad=" + maxLoad +
                ", volumeLong=" + volumeLong +
                ", volumeWidth=" + volumeWidth +
                ", volumeHeight=" + volumeHeight +
                '}';
    }
}
