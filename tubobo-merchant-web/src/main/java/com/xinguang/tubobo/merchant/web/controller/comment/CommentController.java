package com.xinguang.tubobo.merchant.web.controller.comment;

import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.rate.ReqComment;
import com.xinguang.tubobo.rate.api.TbbRateResponse;
import com.xinguang.tubobo.rate.api.TbbRateService;
import com.xinguang.tubobo.rate.api.resp.RateContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/17.
 */
@Controller
@RequestMapping(value = "/order/comment")
public class CommentController extends MerchantBaseController<ReqComment,Object> {
    @Autowired
    TbbRateService tbbRateService;
    @Autowired
    OrderService orderService;
    @Override
    protected Object doService(String userId, ReqComment req) throws MerchantClientException {
        logger.info("收到评价请求：userId:{},req:{}",userId,req.toString());
        MerchantOrderEntity orderEntity = orderService.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (null == orderEntity ){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if (EnumMerchantOrderStatus.RATED.getValue().equals(orderEntity.getOrderStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_RATED);
        }
        if (!EnumMerchantOrderStatus.FINISH.getValue().equals(orderEntity.getOrderStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_FINISH);
        }
        Map<String,Integer> map = new HashMap<>();
        map.put(MerchantConstants.KEY_RATE_DELIVERY_SCORE,req.getDeliveryScore());
        map.put(MerchantConstants.KEY_RATE_SERVICE_SCORE,req.getServiceScore());
        TbbRateResponse<RateContent> response = tbbRateService.rate(MerchantConstants.PLATFORM_ID,req.getOrderNo(),orderEntity.getRiderId(),
                MerchantConstants.TARGET_TYPE_RIDER,userId,false,req.getContent(),map);
        if (response.isSucceeded()||
                TbbRateResponse.ErrorCode.ERROR_DUPLICATE_RATE.getCode().equals(response.getErrorCode())){
            if (response.isSucceeded()){
                logger.info("评价系统评价成功,userId:{},req:{}",userId,req.toString());
            }else {
                logger.info("评价系统重复评价，只更新本地数据,userId:{},req:{}",userId,req.toString());
            }
            boolean result = orderService.rateOrder(req.getOrderNo());
            if (result){
                return "";
            }
        }
        logger.error("评价失败：errorCode:{},errorMsg:{}",response.getErrorCode(),response.getMessage());
        throw new MerchantClientException(EnumRespCode.FAIL);
    }
}
