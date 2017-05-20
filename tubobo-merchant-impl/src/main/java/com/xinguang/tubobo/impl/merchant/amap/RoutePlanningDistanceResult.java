package com.xinguang.tubobo.impl.merchant.amap;

import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
public class RoutePlanningDistanceResult {
    private int status;
    private String info;
    private List<RoutePlaningDistanceItem> results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<RoutePlaningDistanceItem> getResults() {
        return results;
    }

    public void setResults(List<RoutePlaningDistanceItem> results) {
        this.results = results;
    }
}
