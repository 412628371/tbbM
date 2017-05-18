package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.LocationUtil;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Created by Administrator on 2017/4/14.
 */
@Service
public class DeliveryFeeService  {

    @Autowired
    MerchantInfoService merchantInfoService;
    @Resource
    Config config;

    public double sumDeliveryFeeByDistance(double distance) throws MerchantClientException {
        double distanceInKm = Math.ceil((distance/1000));
        double fee = config.getFirstLevelInitPrice();
        double firstLevelPricePerKM = config.getFirstLevelPricePerKM();
        double secondLevelDistance = config.getSecondLevelDistance();
        double secondLevelPricePerKM = config.getSecondLevelPricePerKM();
//        double initDistanceByMiles = config.getInitDistanceByMiles();
//        double pricePerKiloMiles = config.getPricePerKiloMiles();
        //TODO 配置,
        if (distanceInKm <= 1){
            return fee;
        }else {
//            distanceInKm-=1;
            if (distanceInKm-1 <= secondLevelDistance){
                fee += (distanceInKm-1) * firstLevelPricePerKM;
            }else {
                fee+= (secondLevelDistance-1)*firstLevelPricePerKM;
                fee+= (distanceInKm-secondLevelDistance)*secondLevelPricePerKM;
            }

        }
//        double overDistanceFee = Math.ceil((distance/1000))*pricePerKiloMiles;
//        fee += overDistanceFee;
        return fee;
    }

    public double sumDeliveryFeeByLocation(String userId,Double lat,Double lng,String goodsType) throws MerchantClientException {
        double distance = sumDeliveryDistance(userId,lat,lng);
        return sumDeliveryFeeByDistance(distance);

    }
    public double sumDeliveryDistance(String userId,Double lat,Double lng) throws MerchantClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity)
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        if (lat==null || lng == null){
            throw new MerchantClientException(EnumRespCode.PARAMS_ERROR);
        }
        double distance = LocationUtil.getDistance(lat,lng,entity.getLatitude(),entity.getLongitude());
        if (distance > config.getMaxDeliveryMills()){
            throw new MerchantClientException(EnumRespCode.MERCHANT_DELIVERY_DISTANCE_TOO_FAR);
        }
        return distance;
    }
}
