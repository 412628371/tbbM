package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.TbbConstants;
import com.xinguang.tubobo.account.api.request.RechargeRequest;
import com.xinguang.tubobo.account.api.response.RechargeInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.service.MerchantTypeService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqAccountRecharge;
import com.xinguang.tubobo.merchant.web.response.trade.RespAccountRecharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by Administrator on 2017/4/15.
 */
@Controller
@RequestMapping("/account/recharge")
public class MerchantAccountRechargrController extends MerchantBaseController<ReqAccountRecharge,RespAccountRecharge> {

    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantTypeService merchantTypeService;

    @Autowired
    private Config config;

    @Override
    protected boolean needIdentify() {
        return true;
    }

    @Override
    protected RespAccountRecharge doService(String userId, ReqAccountRecharge req) throws MerchantClientException {

        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (entity == null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        if (config.isSysInterfaceCloseFlag() && null != entity.getAddressAdCode() &&
                entity.getAddressAdCode().startsWith(config.getRechargeForbiddenArea())){
            throw new MerchantClientException(EnumRespCode.INTERFACE_NOT_SUPPORT);
        }
        RechargeRequest rechargeRequest = new RechargeRequest();
        rechargeRequest.setAmount(ConvertUtil.convertYuanToFen(req.getAmount()));
        rechargeRequest.setAccountId(entity.getAccountId());
        rechargeRequest.setTarget(TbbConstants.OperationTarget.BALANCE);
        rechargeRequest.setClientIp(ConvertUtil.handleNullString(req.getClientIp()));
        rechargeRequest.setTitle(MerchantConstants.MERCHANT_RECHARGE_TITLE);
        rechargeRequest.setChannel(TbbConstants.Channel.ALI_PAY);
        MerchantTypeDTO tyepInfo = merchantTypeService.findById(entity.getMerTypeId());
        rechargeRequest.setMerchantType(tyepInfo.getName());
        TbbAccountResponse<RechargeInfo> response = tbbAccountService.recharge(rechargeRequest);
        if (response != null && response.isSucceeded()){
            RespAccountRecharge respAccountRecharge = new RespAccountRecharge();
            respAccountRecharge.setPayInfo(response.getData().getPayInfo());
            return respAccountRecharge;
        } else {
            if (response != null){
                logger.error("充值失败，userId: {},errorCode:{},errorMsg:{}",userId,response.getErrorCode(),response.getMessage());
            }else {
                logger.error("充值失败，userId: {}",userId);
            }
            throw  new MerchantClientException(EnumRespCode.ACCOUNT_RECHARGE_FAIL);
        }
    }
}
