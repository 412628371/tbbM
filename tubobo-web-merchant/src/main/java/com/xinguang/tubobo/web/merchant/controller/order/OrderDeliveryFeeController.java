package com.xinguang.tubobo.web.merchant.controller.order;

import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.OrderDeliveryFeeRequest;
import com.xinguang.tubobo.web.merchant.response.OrderDeliveryFeeResponse;
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
    protected OrderDeliveryFeeResponse doService(String userId, OrderDeliveryFeeRequest req) throws ClientException {
        Double fee = deliveryFeeService.sumDeliveryDistance(userId,req.getReceiverLatitude(),
                req.getReceiverLongitude(),req.getGoodsType());
        OrderDeliveryFeeResponse response = new OrderDeliveryFeeResponse();
        response.setDeliveryFee(fee);
        return response;
    }
}
