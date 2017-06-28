package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.dao.MerchantInfoDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.DeliveryFeeService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumOrderType;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.OrderDeliveryFeeRequest;
import com.xinguang.tubobo.merchant.web.request.order.OverFeeInfoRequest;
import com.xinguang.tubobo.merchant.web.response.order.OrderDeliveryFeeResponse;
import com.xinguang.tubobo.merchant.web.response.order.OrderOverFeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/caculateOverFee")
public class OrderOverFeeController extends MerchantBaseController<OverFeeInfoRequest,OrderOverFeeResponse>{



    @Override
    protected OrderOverFeeResponse doService(String userId, OverFeeInfoRequest req) throws MerchantClientException {

        OrderOverFeeResponse orderOverFeeResponse = new OrderOverFeeResponse();
        orderOverFeeResponse.setPeekOverFee(1.0);
        orderOverFeeResponse.setTotalOverFee(2.5);
        orderOverFeeResponse.setWeatherOverFee(1.5);
        return orderOverFeeResponse;
    }
}
