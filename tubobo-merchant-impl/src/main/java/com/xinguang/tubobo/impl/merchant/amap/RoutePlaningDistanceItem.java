package com.xinguang.tubobo.impl.merchant.amap;

/**
 * Created by Administrator on 2017/5/20.
 */
public class RoutePlaningDistanceItem {

    private Integer origin_id;
    private Integer dest_id;
    private Double distance;
    private Long duration;

    public Integer getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(Integer origin_id) {
        this.origin_id = origin_id;
    }

    public Integer getDest_id() {
        return dest_id;
    }

    public void setDest_id(Integer dest_id) {
        this.dest_id = dest_id;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "RoutePlaningDistanceItem{" +
                "origin_id=" + origin_id +
                ", dest_id=" + dest_id +
                ", distance=" + distance +
                ", duration=" + duration +
                '}';
    }
}
