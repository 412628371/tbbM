package com.xinguang.tubobo.merchant.web.controller.order;

import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqOrderOperate;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
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
    MerchantOrderService merchantOrderService;
    @Override
    protected EnumRespCode doService(String userId, ReqOrderOperate req) throws MerchantClientException {
        MerchantOrderEntity entity = merchantOrderService.findByMerchantIdAndOrderNo(userId,req.getOrderNo());
        if (null == entity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        if (MerchantConstants.REQ_ORDER_CANCEL.equals(req.getCommand())){
            boolean count = merchantOrderService.meachantCancel(userId,req.getOrderNo());
            if (!count){
                throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_CANCEL);
            }
        }
        if (MerchantConstants.REQ_ORDER_DELETE.equals(req.getCommand())){
            int count = merchantOrderService.deleteOrder(userId,req.getOrderNo());
            if (count == 0){
                throw new MerchantClientException(EnumRespCode.MERCHANT_CANT_DELETE);
            }
        }
        return EnumRespCode.SUCCESS;
    }
}
