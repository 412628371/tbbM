package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.rate.api.TbbRateResponse;
import com.xinguang.tubobo.rate.api.hystrix.TbbRateServiceHystrixProxy;
import com.xinguang.tubobo.rate.api.resp.RateContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 评价服务.
 */
@Service
public class RateService {
    Logger logger = LoggerFactory.getLogger(RateService.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    TbbRateServiceHystrixProxy tbbRateService;

    public boolean rate(String userId,String orderNo,int deliveryScore,int serviceScore,String content,String riderId){
        Map<String,Integer> map = new HashMap<>();
        map.put(MerchantConstants.KEY_RATE_DELIVERY_SCORE,deliveryScore);
        map.put(MerchantConstants.KEY_RATE_SERVICE_SCORE,serviceScore);
        TbbRateResponse<RateContent> response = tbbRateService.rate(MerchantConstants.PLATFORM_ID,
                orderNo,riderId,
                MerchantConstants.TARGET_TYPE_RIDER,userId,false,content,map);
        if (response.isSucceeded()||
                TbbRateResponse.ErrorCode.ERROR_DUPLICATE_RATE.getCode().equals(response.getErrorCode())){
            if (response.isSucceeded()){
                logger.info("评价系统评价成功,userId:{},orderNo:{}",userId,orderNo);
            }else {
                logger.info("评价系统重复评价，只更新本地数据,userId:{},orderNo:{}",userId,orderNo);
            }
            boolean result = orderService.rateOrder(userId,orderNo);
            return result;
        }else {
            logger.error("评价失败：errorCode:{},errorMsg:{}",response.getErrorCode(),response.getMessage());
            return false;
        }
    }
}
