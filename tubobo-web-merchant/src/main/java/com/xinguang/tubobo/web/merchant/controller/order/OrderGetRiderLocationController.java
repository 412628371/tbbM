package com.xinguang.tubobo.web.merchant.controller.order;

import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.api.merchant.TaskCenterToMerchantServiceInterface;
import com.xinguang.tubobo.api.merchant.dto.GeoLocation;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ReqOrderRiderLocation;
import com.xinguang.tubobo.web.merchant.response.RespRiderLocation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/17.
 */
@Controller
@RequestMapping("/order/riderLocation")
public class OrderGetRiderLocationController extends MerchantBaseController<ReqOrderRiderLocation,RespRiderLocation>{
    @Autowired
    private MerchantOrderService merchantOrderService;
    @Autowired
    private TaskCenterToMerchantServiceInterface taskCenterToMerchantServiceInterface;
    @Override
    protected RespRiderLocation doService(String userId, ReqOrderRiderLocation req) throws ClientException {
        MerchantOrderEntity entity = merchantOrderService.findByOrderNo(req.getOrderNo());
        if (entity == null){
            throw new ClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        GeoLocation location =taskCenterToMerchantServiceInterface.getRiderLocation(entity.getRiderId());
        if (null == location){
            throw new ClientException(EnumRespCode.MERCHANT_RIDER_LOCATION_NOT_FOUND);
        }
        RespRiderLocation respRiderLocation = new RespRiderLocation();
        BeanUtils.copyProperties(location,respRiderLocation);
        return respRiderLocation;
    }
}
