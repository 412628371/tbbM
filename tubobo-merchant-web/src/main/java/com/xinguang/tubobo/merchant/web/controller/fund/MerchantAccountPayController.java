package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.merchant.web.request.ReqAccountPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/15.
 */
@Controller
@RequestMapping("/account/pay")
public class MerchantAccountPayController extends MerchantBaseController<ReqAccountPay,Object> {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    private MerchantOrderService merchantOrderService;
    @Override
    protected EnumRespCode doService(String userId, ReqAccountPay req) throws MerchantClientException {
        logger.info("支付请求：userId:{}, orderNo:{} , ",userId,req.getOrderNo());
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        MerchantOrderEntity orderEntity = merchantOrderService.findByOrderNo(req.getOrderNo());
        if (null == orderEntity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        PayRequest payRequest = new PayRequest();
        payRequest.setAccountId(infoEntity.getAccountId());
        payRequest.setAmount(ConvertUtil.convertYuanToFen(orderEntity.getPayAmount()));
        logger.info("支付请求：userId:{}, orderNo:{} ,amount:{}分 ",userId,req.getOrderNo(),payRequest.getAmount());
//        payRequest.setOrderId();
        TbbAccountResponse<PayInfo> response = tbbAccountService.pay(payRequest);
        if (response != null && response.isSucceeded()){
            long payId = response.getData().getId();

            merchantOrderService.merchantPay(infoEntity.getUserId(),req.getOrderNo(),payId);
            logger.info("pay  SUCCESS. orderNo:{}, accountId:{}, payId:{}, amount:{}",req.getOrderNo()
                    ,infoEntity.getAccountId(),response.getData().getId(),payRequest.getAmount());
            return null;
        }else {
            if (response == null){
                logger.error("pay  FAIL.orderNo:{}, accountId:{}}",
                        req.getOrderNo(),infoEntity.getAccountId());
            }else {
                logger.error("pay  FAIL.orderNo:{}, accountId:{}, errorCode:{}, errorMsg{}",
                        req.getOrderNo(),infoEntity.getAccountId(),response.getErrorCode(),response.getMessage());
            }
            throw  new MerchantClientException(EnumRespCode.ACCOUNT_PAY_FAIL);
        }
    }
}
