package com.xinguang.tubobo.merchant.web.manager;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderDetail;
import com.xinguang.tubobo.merchant.web.response.order.v2.RespOrderDetailV2;
import com.xinguang.tubobo.rate.api.TbbRateResponse;
import com.xinguang.tubobo.rate.api.TbbRateService;
import com.xinguang.tubobo.rate.api.resp.RateContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


/**
 * Created by Administrator on 2017/6/5.
 */
@Service
public class OrderDetailRespManager {
    Logger logger = LoggerFactory.getLogger(OrderDetailRespManager.class);
    @Autowired
    OrderService orderService;
    @Autowired
    TbbRateService tbbRateService;
    @Resource
    Config config;

    public MerchantOrderEntity getOrderEntity(String userId,String orderNo) throws MerchantClientException {
        MerchantOrderEntity entity = orderService.findByMerchantIdAndOrderNo(userId,orderNo);
        if(null == entity){
            logger.error("获取订单详情，订单不存在。orderNo:{}",orderNo);
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        return entity;
    }
    public RespOrderDetail getDetail(String userId,String orderNo,MerchantOrderEntity entity) throws MerchantClientException {
        logger.info("订单详情查询：userId:{},orderNo:{},status:{}",userId,orderNo,entity.getOrderStatus());
        Date now = new Date();
        int grabMilSeconds = config.getTaskGrabExpiredMilSeconds();
        int payMilSeconds = config.getPayExpiredMilSeconds();

        if (EnumOrderType.BIGORDER.getValue().equals(entity.getOrderType())){
            grabMilSeconds = config.getConsignorTaskExpiredMilliSeconds();
            payMilSeconds = config.getConsignorPayExpiredMilliSeconds();
        }
        Long payRemainMilSeconds = 0L;
        Long grabRemainMilSeconds = 0L;
        if (entity.getOrderTime()!=null &&
                entity.getOrderTime().getTime()+payMilSeconds > now.getTime()){
            payRemainMilSeconds = entity.getOrderTime().getTime()+payMilSeconds-now.getTime();
        }

        if (entity.getPayTime()!=null &&
                entity.getPayTime().getTime()+grabMilSeconds > now.getTime()){
            grabRemainMilSeconds = entity.getPayTime().getTime()+grabMilSeconds-now.getTime();
        }
//        payRemainMilSeconds += 2000;
//        grabRemainMilSeconds += 2000;

        handleTimeoutDelay(entity,payRemainMilSeconds,grabRemainMilSeconds);

        RespOrderDetail respOrderDetail = new RespOrderDetail();
        BeanUtils.copyProperties(entity,respOrderDetail);
        respOrderDetail.setOrderRemarks(ConvertUtil.handleNullString(entity.getOrderRemark()));
        respOrderDetail.setPayRemainMillSeconds(payRemainMilSeconds);


        respOrderDetail.setGrabRemainMillSeconds(grabRemainMilSeconds);

        //已评价的订单，获取评价内容
        if (entity.getRatedFlag()){
            TbbRateResponse<RateContent> response = tbbRateService.get(MerchantConstants.PLATFORM_ID,entity.getOrderNo(),entity.getRiderId());
            if (response.isSucceeded()){
                RateContent rateContent = response.getData();
                if (null != rateContent){
                    Map<String,Integer> scores = rateContent.getScores();
                    respOrderDetail.setCommentContent(rateContent.getFullTxt());
                    if (null != scores){
                        respOrderDetail.setCommentDeliveryScore(scores.get(MerchantConstants.KEY_RATE_DELIVERY_SCORE));
                        respOrderDetail.setCommentServiceScore(scores.get(MerchantConstants.KEY_RATE_SERVICE_SCORE));
                    }
                }
            }
        }

        return respOrderDetail;
    }

    /**
     * 处理是否超时取消中
     * @param entity
     * @param payRemainMilSeconds
     * @param grabRemainMilSeconds
     * @throws MerchantClientException
     */
    private void handleTimeoutDelay(MerchantOrderEntity entity,long payRemainMilSeconds,
                                    long grabRemainMilSeconds) throws MerchantClientException {
        if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
            if (payRemainMilSeconds<=0){
                orderService.clearRedisCache(entity.getUserId());
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNPAY_CNACELING);
            }
        }
        if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
            if (grabRemainMilSeconds<=0){
                orderService.clearRedisCache(entity.getUserId());
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNGRAB_CANCELING);
            }
        }
    }
}
