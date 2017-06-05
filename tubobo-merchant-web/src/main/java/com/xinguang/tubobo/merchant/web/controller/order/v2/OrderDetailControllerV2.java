package com.xinguang.tubobo.merchant.web.controller.order.v2;

import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.common.OrderBeanToAddressInfoHelper;
import com.xinguang.tubobo.merchant.web.common.info.*;
import com.xinguang.tubobo.merchant.web.manager.OrderDetailRespManager;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderDetail;
import com.xinguang.tubobo.merchant.web.response.order.RespOrderDetail;
import com.xinguang.tubobo.merchant.web.response.order.v2.RespOrderDetailV2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 订单详情controller 2.0.
 */
@Controller
@RequestMapping("/2.0/order/detail")
public class OrderDetailControllerV2 extends MerchantBaseController<ReqOrderDetail,RespOrderDetailV2> {
    @Autowired
    OrderDetailRespManager manager;

    @Override
    protected RespOrderDetailV2 doService(String userId, ReqOrderDetail req) throws MerchantClientException {

        String orderNo = req.getOrderNo();
        MerchantOrderEntity entity = manager.getOrderEntity(userId,orderNo);
        RespOrderDetail respOrderDetail = manager.getDetail(userId,orderNo,entity);

        AddressInfo consignor = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(entity,new AddressInfo(),true);
        AddressInfo receiver = OrderBeanToAddressInfoHelper.putAddressFromOrderBean(entity,new AddressInfo(),false);
        PayInfo payInfo = new PayInfo();
        BeanUtils.copyProperties(respOrderDetail,payInfo);

        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(respOrderDetail,orderInfo);
        CommentsInfo commentsInfo = new CommentsInfo(respOrderDetail.getRatedFlag());
        BeanUtils.copyProperties(respOrderDetail,commentsInfo);

        Double latitude = null;
        Double longitude = null;
        DriverInfo driverInfo = new DriverInfo(entity.getRiderName(),entity.getRiderPhone(),entity.getRiderCarNo(),
                entity.getRiderCarType(),latitude,longitude);
        RespOrderDetailV2 resp = new RespOrderDetailV2();
        resp.setCommentsInfo(commentsInfo);
        resp.setConsignor(consignor);
        resp.setReceiver(receiver);
        resp.setDriverInfo(driverInfo);
        resp.setOrderInfo(orderInfo);
        resp.setPayInfo(payInfo);
        return resp;
    }

}
