package com.xinguang.tubobo.web.merchant.controller.order;

import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ReqOrderOperate;
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
    protected EnumRespCode doService(String userId, ReqOrderOperate req) throws ClientException {
        if (MerchantConstants.REQ_ORDER_CANCEL.equals(req.getCommand())){
            boolean count = merchantOrderService.meachantCancel(userId,req.getOrderNo());
            if (!count){
                throw new ClientException(EnumRespCode.MERCHANT_CANT_CANCEL);
            }
        }
        if (MerchantConstants.REQ_ORDER_DELETE.equals(req.getCommand())){
            int count = merchantOrderService.deleteOrder(userId,req.getOrderNo());
            if (count == 0){
                throw new ClientException(EnumRespCode.MERCHANT_CANT_DELETE);
            }
        }
        return EnumRespCode.SUCCESS;
    }
}
