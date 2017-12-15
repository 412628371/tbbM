package com.xinguang.tubobo.merchant.web.controller.order;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.order.ReqOrderOperate;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.manager.MerchantOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/15.
 */
@Controller
@RequestMapping("/order/operate")
public class OrderOperateController extends MerchantBaseController<ReqOrderOperate,EnumRespCode> {
    @Autowired
    MerchantOrderManager merchantOrderManager;
    @Override
    protected EnumRespCode doService(String userId, ReqOrderOperate req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderManager.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (null == entity){
            logger.error("订单操作(删除，取消)，订单不存在。orderNo:{}",req.getOrderNo());
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }

        if (MerchantConstants.REQ_ORDER_CANCEL.equals(req.getCommand())){
            // 已接单取消订单 根据WaitPickCancelType不为null判断
            boolean countCancel = merchantOrderManager.cancelOrder(userId,req.getOrderNo(),false,req.getWaitPickCancelType());
            if (!countCancel){
                logger.error("订单操作(删除，取消)，订单不允许取消。orderNo:{}",req.getOrderNo());
                throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_CANCEL);
            }
        }
        if (MerchantConstants.REQ_ORDER_DELETE.equals(req.getCommand())){
            int count = merchantOrderManager.deleteOrder(userId,req.getOrderNo());
            if (count == 0){
                throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_DELETE);
            }
        }
        return EnumRespCode.SUCCESS;
    }
}
