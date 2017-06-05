package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderDetail;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderDetail;
import com.xinguang.tubobo.rate.api.TbbRateResponse;
import com.xinguang.tubobo.rate.api.TbbRateService;
import com.xinguang.tubobo.rate.api.resp.RateContent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/detail")
public class OrderDetailController extends MerchantBaseController<ReqOrderDetail,RespOrderDetail> {
    @Autowired
    OrderService orderService;
    @Autowired
    TbbRateService tbbRateService;
    @Resource
    Config config;
    @Override
    protected RespOrderDetail doService(String userId, ReqOrderDetail req) throws MerchantClientException {
        MerchantOrderEntity entity = orderService.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if(null == entity){
            logger.error("获取订单详情，订单不存在。orderNo:{}",req.getOrderNo());
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        logger.info("订单详情查询：userId:{},orderNo:{},status:{}",userId,req.getOrderNo(),entity.getOrderStatus());
        Date now = new Date();
        handleTimeoutDelay(entity,now);

        RespOrderDetail respOrderDetail = new RespOrderDetail();
        BeanUtils.copyProperties(entity,respOrderDetail);
        respOrderDetail.setPayExpiredMilSeconds(config.getPayExpiredMilSeconds());
        respOrderDetail.setGrabExpiredStartTime(entity.getPayTime());
        respOrderDetail.setGrabExpiredMilSeconds(config.getTaskGrabExpiredMilSeconds());
        respOrderDetail.setOrderRemarks(ConvertUtil.handleNullString(entity.getOrderRemark()));

        Long payRemainMilSeconds = 0L;
        if (entity.getOrderTime()!=null &&
                entity.getOrderTime().getTime()+config.getPayExpiredMilSeconds() > now.getTime()){
            payRemainMilSeconds = entity.getOrderTime().getTime()+config.getPayExpiredMilSeconds()-now.getTime();
        }
        respOrderDetail.setPayRemainMillSeconds(payRemainMilSeconds);

        Long grabRemainMilSeconds = 0L;
        if (entity.getPayTime()!=null &&
                entity.getPayTime().getTime()+config.getTaskGrabExpiredMilSeconds() > now.getTime()){
            grabRemainMilSeconds = entity.getPayTime().getTime()+config.getTaskGrabExpiredMilSeconds()-now.getTime();
        }
        respOrderDetail.setGrabRemainMillSeconds(grabRemainMilSeconds);

        //已评价的订单，获取评价内容
        if (entity.getRatedFlag()){
            TbbRateResponse<RateContent> response = tbbRateService.get(MerchantConstants.PLATFORM_ID,entity.getOrderNo(),entity.getRiderId());
            if (response.isSucceeded()){
                RateContent rateContent = response.getData();
                Map<String,Integer> scores = rateContent.getScores();
                respOrderDetail.setCommentContent(rateContent.getFullTxt());
                if (null != scores){
                    respOrderDetail.setCommentDeliveryScore(scores.get(MerchantConstants.KEY_RATE_DELIVERY_SCORE));
                    respOrderDetail.setCommentServiceScore(scores.get(MerchantConstants.KEY_RATE_SERVICE_SCORE));
                }
            }
        }

        return respOrderDetail;
    }
    private void handleTimeoutDelay(MerchantOrderEntity entity,Date now) throws MerchantClientException {
        if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
            Date orderTime = entity.getOrderTime();
            long expectedExpiredMilSeconds = orderTime.getTime() + config.getPayExpiredMilSeconds();
            if (now.getTime() >= expectedExpiredMilSeconds){
                orderService.clearRedisCache(entity.getUserId());
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNPAY_CNACELING);
            }
        }
        if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
            Date payTime = entity.getPayTime();
            long expectedExpiredMilSeconds = payTime.getTime() + config.getTaskGrabExpiredMilSeconds();
            if (now.getTime() >= expectedExpiredMilSeconds){
                orderService.clearRedisCache(entity.getUserId());
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNGRAB_CANCELING);
            }
        }
    }
}
