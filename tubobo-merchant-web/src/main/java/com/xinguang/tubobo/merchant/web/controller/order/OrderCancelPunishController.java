package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.taskcenter.api.TaskDispatchService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.order.OrderCancelPunishResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lvhantai on 2017/9/5.
 * 查询取消赔付金额v1.4.3
 */
@Controller
@RequestMapping("/merchant/cancelPunish")
public class OrderCancelPunishController extends MerchantBaseController<Object, OrderCancelPunishResponse>{
    @Autowired
    private TaskDispatchService taskDispatchService;

    @Override
    protected OrderCancelPunishResponse doService(String userId, Object req) throws MerchantClientException {
        OrderCancelPunishResponse response = new OrderCancelPunishResponse();
        response.setCancelPrice(taskDispatchService.getCancelPrice().getData());
        return response;
    }
}
