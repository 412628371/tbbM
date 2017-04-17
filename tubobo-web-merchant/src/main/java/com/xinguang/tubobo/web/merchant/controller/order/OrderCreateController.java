package com.xinguang.tubobo.web.merchant.controller.order;

import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.api.enums.EnumPayStatus;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.CreateOrderRequest;
import com.xinguang.tubobo.web.merchant.response.CreateOrderResponse;
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
    MerchantOrderService merchantOrderService;
    @Override
    protected CreateOrderResponse doService(String userId, CreateOrderRequest req) throws ClientException {
        MerchantOrderEntity entity = translateRequestToEntity(userId,req);
        String orderNo = merchantOrderService.order(userId,entity);
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
        entity.setDispatchRadius(MerchantConstants.DISPATCH_RADIUS_BY_MILLS);

        entity.setDelFlag(MerchantOrderEntity.DEL_FLAG_NORMAL);
        return entity;
    }
}
