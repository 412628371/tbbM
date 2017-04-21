package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.api.TaskCenterToMerchantServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.GeoLocation;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.merchant.web.request.ReqOrderRiderLocation;
import com.xinguang.tubobo.merchant.web.response.RespRiderLocation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/17.
 */
@Controller
@RequestMapping("/order/riderLocation")
public class OrderGetRiderLocationController extends MerchantBaseController<ReqOrderRiderLocation,RespRiderLocation> {
    @Autowired
    private MerchantOrderService merchantOrderService;
    @Autowired
    private TaskCenterToMerchantServiceInterface taskCenterToMerchantServiceInterface;
    @Override
    protected RespRiderLocation doService(String userId, ReqOrderRiderLocation req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderService.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (entity == null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        GeoLocation location =taskCenterToMerchantServiceInterface.getRiderLocation(entity.getRiderId());
        if (null == location){
            throw new MerchantClientException(EnumRespCode.MERCHANT_RIDER_LOCATION_NOT_FOUND);
        }
        RespRiderLocation respRiderLocation = new RespRiderLocation();
        BeanUtils.copyProperties(location,respRiderLocation);
        return respRiderLocation;
    }
}
