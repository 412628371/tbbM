package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.amap.RoutePlanning;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.request.order.OrderDeliveryFeeRequest;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.order.OrderDeliveryFeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/calculateDeliveryFee")
public class OrderDeliveryFeeController extends MerchantBaseController<OrderDeliveryFeeRequest,OrderDeliveryFeeResponse>{
    @Autowired
    private DeliveryFeeService deliveryFeeService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private RoutePlanning routePlanning;
    @Override
    protected OrderDeliveryFeeResponse doService(String userId, OrderDeliveryFeeRequest req) throws MerchantClientException {
        logger.info("计算配送费请求, userId:{}, OrderDeliveryFeeRequest:{}",userId,req.toString());
        Double distance;
        Double fee;
        if (EnumOrderType.BIGORDER.getValue().equals(req.getOrderType())){
            //获取实际距离
            distance = routePlanning.getDistanceWithWalkFirst(req.getReceiverLongitude(),req.getReceiverLatitude(),
                    req.getSenderLongitude(),req.getSenderLatitude());
            fee = deliveryFeeService.sumChepeiFee(req.getCarType(), distance);
        }else {
            //获取商家信息
            MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
            if (null == entity)
                throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
            if (req.getReceiverLatitude()==null || req.getReceiverLongitude() == null){
                throw new MerchantClientException(EnumRespCode.PARAMS_ERROR);
            }
            //获取实际距离
            distance = routePlanning.getDistanceWithWalkFirst(req.getReceiverLongitude(),req.getReceiverLatitude(),
                    entity.getLongitude(),entity.getLatitude());
            fee = deliveryFeeService.sumDeliveryFeeByLocation(distance, entity.getAddressAdCode(),null);
        }

        OrderDeliveryFeeResponse response = new OrderDeliveryFeeResponse();
        response.setDeliveryDistance(distance);
        response.setDeliveryFee(fee);
        logger.info("计算配送费相应, userId:{}, deliveryFee:{},deliverDistance:{}",userId,fee,distance);
        return response;
    }
}
