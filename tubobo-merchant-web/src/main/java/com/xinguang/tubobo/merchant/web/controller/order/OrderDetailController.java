package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqOrderDetail;
import com.xinguang.tubobo.merchant.web.response.RespOrderDetail;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
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
    MerchantOrderService merchantOrderService;
    @Resource
    Config config;
    @Override
    protected RespOrderDetail doService(String userId, ReqOrderDetail req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderService.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if(null == entity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if (EnumMerchantOrderStatus.INIT.getValue().equals(entity.getOrderStatus())){
            Date orderTime = entity.getOrderTime();
            long expectedExpiredMilSeconds = orderTime.getTime() + config.getPayExpiredMilSeconds();
            if (new Date().getTime() >= expectedExpiredMilSeconds){
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNPAY_CNACELING);
            }
        }
        if (EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(entity.getOrderStatus())){
            Date payTime = entity.getPayTime();
            long expectedExpiredMilSeconds = payTime.getTime() + config.getTaskGrabExpiredMilSeconds();
            if (new Date().getTime() >= expectedExpiredMilSeconds){
                throw new MerchantClientException(EnumRespCode.MERCHANT_UNGRAB_CANCELING);
            }
        }
        RespOrderDetail respOrderDetail = new RespOrderDetail();
        BeanUtils.copyProperties(entity,respOrderDetail);
        respOrderDetail.setPayExpiredMilSeconds(config.getPayExpiredMilSeconds());
        respOrderDetail.setGrabExpiredStartTime(entity.getPayTime());
        respOrderDetail.setGrabExpiredMilSeconds(config.getTaskGrabExpiredMilSeconds());
        return respOrderDetail;
    }
}
