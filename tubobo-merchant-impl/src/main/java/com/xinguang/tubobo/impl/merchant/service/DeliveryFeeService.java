package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.api.AdminToMerchantService;
import com.xinguang.tubobo.api.dto.CarTypeDTO;
import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;



/**
 * Created by Administrator on 2017/4/14.
 */
@Service
public class DeliveryFeeService  {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryFeeService.class);
    @Autowired
    MerchantInfoService merchantInfoService;
    @Resource
    Config config;

    @Autowired
    RoutePlanning routePlanning;

    @Autowired
    AdminToMerchantService adminToMerchantService;

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
            if (distanceInKm <= secondLevelDistance){
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
        double distance = sumDeliveryDistanceMerchant(userId,lat,lng);
        return sumDeliveryFeeByDistance(distance);
    }
    public double sumChepeiFee(String carType,double lng1,double lat1,double lng2,double lat2) throws MerchantClientException {
        double distance = routePlanning.getDistanceWithCar(lng1,lat1,lng2,lat2);
        Double distanceByKm = Math.ceil(distance/1000);
        CarTypeDTO carTypeDTO ;
        carTypeDTO =  adminToMerchantService.queryCarTypeInfo(carType);
        if (carTypeDTO == null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_CAR_TYPE_NOT_SUPPORT);
        }
        double startPrice = carTypeDTO.getStartPrice();
        double beyondPrice = carTypeDTO.getBeyondPrice();
        double startDistance = carTypeDTO.getStartDistance();
        double fee = startPrice;
        if (distanceByKm>startDistance){
            distanceByKm-=startDistance;
        }else {
            return fee;
        }
        double overDistanceFee = distanceByKm*beyondPrice;
        fee+=overDistanceFee;
        return fee;
    }
    public double sumDeliveryDistanceMerchant(String userId,Double lat,Double lng) throws MerchantClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity)
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        if (lat==null || lng == null){
            throw new MerchantClientException(EnumRespCode.PARAMS_ERROR);
        }
        double distance = routePlanning.getDistanceWithWalkFirst(lng,lat,entity.getLongitude(),entity.getLatitude());
        if (distance > config.getMaxDeliveryMills()){
            throw new MerchantClientException(EnumRespCode.MERCHANT_DELIVERY_DISTANCE_TOO_FAR);
        }
        return distance;
    }
    public double sumDeliveryDistanceChePei(Double lng1,Double lat1,Double lng2,Double lat2) throws MerchantClientException {
        double distance = routePlanning.getDistanceWithWalkFirst(lng1,lat1,lng2,lat2);
        return distance;
    }
    /**
     * 计算配送时效
     * @param distance
     * @return
     */
    public Double sumDistributionLimitation(Double distance){
        try{
            String expression = config.getDistributionLimitationExpression();
            if (StringUtils.isBlank(expression)){
                //TODO
            }
            String[] es = expression.split(MerchantConstants.DEFAULT_SEPARATOR);
            ArrayList<Double> distanceList = new ArrayList(es.length);
            ArrayList<Double> minutesList = new ArrayList(es.length);
            for(String unitEp :es){
                String[] kvs = unitEp.split(":");
                distanceList.add(Double.valueOf(kvs[0]));
                minutesList.add(Double.valueOf(kvs[1]));
            }
            int index = -1;
            for (int i = 0;i< distanceList.size()-1;i++){
                if (distance<= distanceList.get(i)){
                    index = i;
                    break;
                }
            }
            if (index == -1){
                index = distanceList.size()-1;
            }
            Double minute = minutesList.get(index);
            return minute;
        }catch (Exception e){
            logger.error("计算配送时效错误，使用默认时间，60分钟");
            return 60.0;
        }

    }
}
