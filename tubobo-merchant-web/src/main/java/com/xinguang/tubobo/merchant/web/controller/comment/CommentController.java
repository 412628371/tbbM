package com.xinguang.tubobo.merchant.web.controller.comment;

import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.impl.merchant.service.RateService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.rate.ReqComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by Administrator on 2017/5/17.
 */
@Controller
@RequestMapping(value = "/order/comment")
public class CommentController extends MerchantBaseController<ReqComment,Object> {
    @Autowired
    RateService rateService;
    @Autowired
    OrderService orderService;
    @Override
    protected Object doService(String userId, ReqComment req) throws MerchantClientException {
        logger.info("收到评价请求：userId:{},req:{}",userId,req.toString());
        MerchantOrderEntity orderEntity = orderService.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (null == orderEntity ){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if (orderEntity.getRatedFlag()){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_RATED);
        }
        if (!EnumMerchantOrderStatus.FINISH.getValue().equals(orderEntity.getOrderStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_FINISH);
        }
       boolean result = rateService.rate(userId,orderEntity.getOrderNo(),req.getDeliveryScore(),req.getServiceScore(),
               req.getContent(),orderEntity.getRiderId());
        if (result){
            return "";
        }
        throw new MerchantClientException(EnumRespCode.FAIL);
    }
}
