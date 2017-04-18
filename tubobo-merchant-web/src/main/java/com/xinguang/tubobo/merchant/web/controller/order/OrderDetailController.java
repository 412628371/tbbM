package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqOrderDetail;
import com.xinguang.tubobo.merchant.web.response.RespOrderDetail;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import org.springframework.beans.BeanUtils;
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
    MerchantOrderService merchantOrderService;
    @Override
    protected RespOrderDetail doService(String userId, ReqOrderDetail req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderService.findByOrderNo(req.getOrderNo());
        RespOrderDetail respOrderDetail = new RespOrderDetail();
        BeanUtils.copyProperties(entity,respOrderDetail);

        return respOrderDetail;
    }
}