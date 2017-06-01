package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.ReqTodayOrderNum;
import com.xinguang.tubobo.merchant.web.response.RespTodayOrderNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/5/16.
 */
@Controller
@RequestMapping("/order/today/number")
public class OrderTodayOrderNumController extends MerchantBaseController<ReqTodayOrderNum,RespTodayOrderNum> {
    @Autowired
    OrderService orderService;

    @Override
    protected RespTodayOrderNum doService(String userId, ReqTodayOrderNum req) throws MerchantClientException {
        Long finishTodayNum = orderService.getTodayFinishOrderNum(userId);
        RespTodayOrderNum respTodayOrderNum = new RespTodayOrderNum();
        respTodayOrderNum.setFinishOrders(finishTodayNum);
        return respTodayOrderNum;
    }
}
