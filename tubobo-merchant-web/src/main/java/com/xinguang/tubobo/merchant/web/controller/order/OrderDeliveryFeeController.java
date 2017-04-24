package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.merchant.web.request.OrderDeliveryFeeRequest;
import com.xinguang.tubobo.merchant.web.response.OrderDeliveryFeeResponse;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/calculateDeliveryFee")
public class OrderDeliveryFeeController extends MerchantBaseController<OrderDeliveryFeeRequest,OrderDeliveryFeeResponse>{

    @Autowired
    DeliveryFeeService deliveryFeeService;
    @Override
    protected OrderDeliveryFeeResponse doService(String userId, OrderDeliveryFeeRequest req) throws MerchantClientException {
        logger.info("计算配送费请求, userId:{}, OrderDeliveryFeeRequest:{}",userId,req.toString());
        Double fee = deliveryFeeService.sumDeliveryFeeByLocation(userId,req.getReceiverLatitude(),
                req.getReceiverLongitude(),req.getGoodsType());
        OrderDeliveryFeeResponse response = new OrderDeliveryFeeResponse();
        response.setDeliveryFee(fee);
        logger.info("计算配送费相应, userId:{}, deliveryFee:{}",userId,fee);
        return response;
    }
}
