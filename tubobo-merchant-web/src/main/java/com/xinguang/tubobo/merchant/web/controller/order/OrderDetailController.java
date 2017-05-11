package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqOrderDetail;
import com.xinguang.tubobo.merchant.web.response.RespOrderDetail;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/detail")
public class OrderDetailController extends MerchantBaseController<ReqOrderDetail,RespOrderDetail> {
    @Autowired
    MerchantOrderManager merchantOrderManager;
    @Resource
    Config config;
    @Override
    protected RespOrderDetail doService(String userId, ReqOrderDetail req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderManager.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        Date now = new Date();
        if(null == entity){
            logger.error("获取订单详情，订单不存在。orderNo:{}",req.getOrderNo());
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
            Date orderTime = entity.getOrderTime();
            long expectedExpiredMilSeconds = orderTime.getTime() + config.getPayExpiredMilSeconds();
            if (now.getTime() >= expectedExpiredMilSeconds){
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNPAY_CNACELING);
            }
        }
        if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
            Date payTime = entity.getPayTime();
            long expectedExpiredMilSeconds = payTime.getTime() + config.getTaskGrabExpiredMilSeconds();
            if (now.getTime() >= expectedExpiredMilSeconds){
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNGRAB_CANCELING);
            }
        }
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
        return respOrderDetail;
    }
}
