package com.hzmux.hzcms.common.utils;

/**
 * Created by Administrator on 2017/4/8.
 */
public class LocationUtil {
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 通过经纬度获取距离(单位：米)
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s*1000;
        return s;
    }

    public static void main(String[] args) {
//        120.184384,30.222984
        Double lngSource = 120.182444;
        Double latSource = 30.232486;
        Double lngTarget = 120.184384;
        Double latTarget = 30.222984;

        double distance = getDistance(latSource,lngSource,latTarget,lngTarget);
        System.out.println("distance :"+distance);
    }
}
