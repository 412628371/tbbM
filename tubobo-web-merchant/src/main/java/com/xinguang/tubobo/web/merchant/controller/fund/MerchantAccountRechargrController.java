package com.xinguang.tubobo.web.merchant.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.TbbConstants;
import com.xinguang.tubobo.account.api.request.RechargeRequest;
import com.xinguang.tubobo.account.api.response.RechargeInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ReqAccountRecharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/15.
 */
@Controller
@RequestMapping("/account/recharge")
public class MerchantAccountRechargrController extends MerchantBaseController<ReqAccountRecharge,EnumRespCode> {

    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Override
    protected EnumRespCode doService(String userId, ReqAccountRecharge req) throws ClientException {

        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (entity == null){
            throw new ClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setAmount(ConvertUtil.convertYuanToFen(req.getAmount()));
        rechargeRequest.setAccountId(entity.getAccountId());
        rechargeRequest.setTarget(TbbConstants.OperationTarget.BALANCE);
        rechargeRequest.setClientIp(ConvertUtil.handleNullString(req.getClientIp()));
        rechargeRequest.setTitle(MerchantConstants.MERCHANT_RECHARGE_TITLE);
        rechargeRequest.setChannel(TbbConstants.Channel.ALI_PAY);
        TbbAccountResponse<RechargeInfo> response = tbbAccountService.recharge(rechargeRequest);
        if (response != null && response.isSucceeded()){
            return EnumRespCode.SUCCESS;
        } else {
            return EnumRespCode.FAIL;
        }
    }
}
