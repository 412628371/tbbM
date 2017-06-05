package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.web.manager.OrderDetailRespManager;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderDetail;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/order/detail")
public class OrderDetailController extends MerchantBaseController<ReqOrderDetail,RespOrderDetail> {
    @Autowired
    OrderDetailRespManager orderDetailRespHelper;
    @Override
    protected RespOrderDetail doService(String userId, ReqOrderDetail req) throws MerchantClientException {
        MerchantOrderEntity entity = orderDetailRespHelper.getOrderEntity(userId,req.getOrderNo());
        return orderDetailRespHelper.getDetail(userId,req.getOrderNo(),entity);
    }
}
