package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
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
    DeliveryFeeService deliveryFeeService;
    @Override
    protected OrderDeliveryFeeResponse doService(String userId, OrderDeliveryFeeRequest req) throws MerchantClientException {
        logger.info("计算配送费请求, userId:{}, OrderDeliveryFeeRequest:{}",userId,req.toString());

        Double fee;
        HashMap<Double, Double> map;
        if (EnumOrderType.BIGORDER.getValue().equals(req.getOrderType())){
            map= deliveryFeeService.sumChepeiFee(req.getCarType(),req.getSenderLongitude(),
                    req.getSenderLatitude(),req.getReceiverLongitude(),req.getReceiverLatitude());
        }else {
            map = deliveryFeeService.sumDeliveryFeeByLocation(userId, req.getReceiverLatitude(),
                    req.getReceiverLongitude(), null);
        }

        OrderDeliveryFeeResponse response = new OrderDeliveryFeeResponse();
        if (map==null||map.size()!=1){
            logger.info("获取配送费配送距离失败");
        }else{
            for (Map.Entry<Double, Double> entry : map.entrySet()) {
                response.setDeliveryDistance(entry.getKey());
                response.setDeliveryFee(entry.getValue());
                logger.info("计算配送费相应, userId:{}, deliveryFee:{},deliverDistance:{}",userId,entry.getValue(),entry.getKey());
            }
        }

        return response;
    }
}
