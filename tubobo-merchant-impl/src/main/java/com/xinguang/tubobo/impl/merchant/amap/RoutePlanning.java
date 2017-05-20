package com.xinguang.tubobo.impl.merchant.amap;

import com.hzmux.hzcms.common.utils.LocationUtil;
import com.xinguang.tubobo.impl.merchant.common.PoolHttpsClientService;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
@Service
public class RoutePlanning {

    Logger logger = LoggerFactory.getLogger(RoutePlanning.class);
    @Autowired
    Config config;

    /**
     * 根据经纬度，获取驾车距离
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public Double getDistanceWithCar(Double lng1,Double lat1,Double lng2,Double lat2){
        String origins = jointLocation(lng1,lat1);
        String destination = jointLocation(lng2,lat2);
        String url = jointRequestString(origins,destination);
        logger.info("路径规划，获取距离。origins:{},destination:{},url:{}",origins,destination,url);
        RoutePlanningDistanceResult result = PoolHttpsClientService.getInstance().get(RoutePlanningDistanceResult.class,url);
        if (result.getStatus() == 1){
            List<RoutePlaningDistanceItem> itemsList = result.getResults();
            if (itemsList != null && itemsList.size() > 0){
                Double distance = itemsList.get(0).getDistance();
                logger.info("路径规划，获取距离distance:{}。origins:{},destination:{},url:{}",distance,origins,destination,url);
                return distance;
            }
        }
        Double distance = LocationUtil.getDistance(lat1,lng1,lat2,lng2);
        logger.info("路径规划，获取距离失败。使用直线距离:{}。status:{},info:{}",distance,result.getStatus(),result.getInfo());
        return distance;
    }
    private String jointLocation(Double lng,Double lat){
        StringBuilder sb = new StringBuilder(String.valueOf(lng));
        sb.append(",").append(String.valueOf(lat));
        return sb.toString();
    }
    private String jointRequestString(String origins,String destination){
        StringBuilder sb = new StringBuilder("http://restapi.amap.com/v3/distance?");
        sb.append("key=").append(config.getGdKey()).append("&")
                .append("origins=").append(origins).append("&")
                .append("destination=").append(destination).append("&");
        return sb.toString();
    }

}
