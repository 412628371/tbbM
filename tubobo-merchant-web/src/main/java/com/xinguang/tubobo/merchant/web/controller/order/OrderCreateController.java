package com.xinguang.tubobo.merchant.web.controller.order;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumPayStatus;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.CreateOrderRequest;
import com.xinguang.tubobo.merchant.web.response.CreateOrderResponse;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;


/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/create")
public class OrderCreateController extends MerchantBaseController<CreateOrderRequest,CreateOrderResponse> {
    @Autowired
    MerchantOrderManager merchantOrderManager;
    @Autowired
    MerchantInfoService merchantInfoService;
    @Autowired
    Config config;
    @Override
    protected CreateOrderResponse doService(String userId, CreateOrderRequest req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        if (!EnumAuthentication.SUCCESS.getValue().equals(infoEntity.getMerchantStatus())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_STATUS_CANT_OPERATE);
        }
        if (!DateUtils.isAfterBeginTimeInOneDay(config.getBeginWorkTime())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_TOO_EARLY);
        }
        if (!DateUtils.isBeforeEndTimeInOneDay(config.getEndWorkTime())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_TOO_LATE);
        }
        MerchantOrderEntity entity = translateRequestToEntity(userId,req);
        String orderNo = merchantOrderManager.order(userId,entity);
        CreateOrderResponse response = new CreateOrderResponse();
        response.setOrderNo(orderNo);
        return response;
    }

    private MerchantOrderEntity translateRequestToEntity(String userId,CreateOrderRequest request){
        MerchantOrderEntity entity = new MerchantOrderEntity();
        BeanUtils.copyProperties(request,entity);
        entity.setUserId(userId);
        entity.setSenderId(userId);
        entity.setOrderStatus(EnumMerchantOrderStatus.INIT.getValue());
        entity.setOrderTime(new Date());
        entity.setPayStatus(EnumPayStatus.UNPAY.getValue());
        entity.setDispatchRadius(config.getDispatchRadiusKiloMiles());
        entity.setOrderRemark(ConvertUtil.handleNullString(request.getOrderRemarks()));
        entity.setDelFlag(MerchantOrderEntity.DEL_FLAG_NORMAL);
        return entity;
    }
}
