package com.xinguang.tubobo.impl.merchant.amap;

import com.hzmux.hzcms.common.utils.LocationUtil;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.lbs.api.GdDistanceService;
import com.xinguang.tubobo.lbs.api.LbsConstants;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/20.
 */
@Service
public class RoutePlanning {

    Logger logger = LoggerFactory.getLogger(RoutePlanning.class);
    @Autowired
    Config config;

    @Autowired
    GdDistanceService gdDistanceService;
    /**
     * 根据经纬度，获取驾车距离
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public Double getDistanceWithWalkFirst(Double lng1,Double lat1,Double lng2,Double lat2){
        Double distance;
        if (LbsConstants.TRANSPORT.WALK.getCode().equals(config.getTransportType())){
            distance = gdDistanceService.getDistance(LbsConstants.TRANSPORT.WALK.getCode(),lng1,lat1,lng2,lat2);
            logger.info("路径规划。使用高德步行距离:{}米。",distance);
            if (distance == null){
                distance = gdDistanceService.getDistance(LbsConstants.TRANSPORT.CAR.getCode(),lng1,lat1,lng2,lat2);
                logger.info("路径规划，使用步行距离失败，使用高德驾车距离:{}米。",distance);
            }
        }else {
            distance = gdDistanceService.getDistance(LbsConstants.TRANSPORT.CAR.getCode(),lng1,lat1,lng2,lat2);
            logger.info("路径规划。使用高德驾车距离:{}米。",distance);
        }
        if (distance == null){
            distance = LocationUtil.getDistance(lat1,lng1,lat2,lng2);
            logger.info("路径规划，获取距离失败。使用直线距离:{}米。",distance);
        }
        return distance;
    }

    public Double getDistanceWithCar(Double lng1,Double lat1,Double lng2,Double lat2) throws MerchantClientException {
        Double distance;
        distance = gdDistanceService.getDistance(LbsConstants.TRANSPORT.CAR.getCode(),lng1,lat1,lng2,lat2);
        logger.info("路径规划。使用高德驾车距离:{}米。",distance);
        if (distance == null){
            throw new MerchantClientException(EnumRespCode.FAIL);
        }
        return distance;
    }
}
