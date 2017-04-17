package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.utils.LocationUtil;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by Administrator on 2017/4/14.
 */
@Service
public class DeliveryFeeService  {

    @Autowired
    MerchantInfoService merchantInfoService;

    public double sumDeliveryDistance(String userId,double lat,double lng,String goodsType) throws ClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity)
            throw new ClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        double distance = LocationUtil.getDistance(lat,lng,entity.getLatitude(),entity.getLongitude());
        double fee = 4;
        //TODO 配置,
        if (distance > 1000){
            distance-=1000;
        }
        double overDistance = Math.round((distance/1000))*2;
        fee += overDistance;
        return fee;
    }
}
