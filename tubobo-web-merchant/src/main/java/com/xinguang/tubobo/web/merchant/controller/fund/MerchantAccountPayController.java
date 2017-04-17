package com.xinguang.tubobo.web.merchant.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ReqAccountPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/15.
 */
@Controller
@RequestMapping("/account/pay")
public class MerchantAccountPayController extends MerchantBaseController<ReqAccountPay,EnumRespCode> {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    private MerchantOrderService merchantOrderService;
    @Override
    protected EnumRespCode doService(String userId, ReqAccountPay req) throws ClientException {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        if (null == infoEntity){
            throw new ClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        MerchantOrderEntity orderEntity = merchantOrderService.findByOrderNo(req.getOrderNo());
        if (null == orderEntity){
            throw new ClientException(EnumRespCode.MERCHANT_ORDER_NOT_EXIST);
        }
        PayRequest payRequest = new PayRequest();
        payRequest.setAccountId(infoEntity.getAccountId());
        payRequest.setAmount(ConvertUtil.convertYuanToFen(orderEntity.getPayAmount()));
//        payRequest.setOrderId();
        TbbAccountResponse<PayInfo> response = tbbAccountService.pay(payRequest);
        if (response != null && response.isSucceeded()){
            long payId = response.getData().getId();

            merchantOrderService.merchantPay(infoEntity.getUserId(),req.getOrderNo(),payId);
            logger.info("withdraw confirm SUCCESS. merchantName:{}, merchantPhone:{}, accountId:{}, withdrawId:{}, amount:{}",
                    infoEntity.getRealName(),infoEntity.getPhone(),infoEntity.getAccountId(),response.getData().getId(),payRequest.getAmount());
            return EnumRespCode.SUCCESS;
        }else {
            logger.error("withdraw confirm FAIL. riderName:{}, riderPhone:{}, accountId:{}, withdrawId:{}, amount:{}, errorCode:{}, errorMsg{}",
                    infoEntity.getRealName(),infoEntity.getPhone(),infoEntity.getAccountId(),response.getData().getId(),payRequest,response.getErrorCode(),response.getMessage());
            return EnumRespCode.ACCOUNT_PAY_FAIL;
        }
    }
}
