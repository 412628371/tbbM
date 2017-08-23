package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantInfoManager;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.ReqShopUpdatePhone;
import com.xinguang.usercentergate.api.TokenServiceInterface;
import com.xinguang.xingchen.SMSplatform.api.SMSServiceInterface;
import com.xinguang.xingchen.SMSplatform.enums.EnumCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 修改联系方式
 */
@Controller
@RequestMapping(value = "/info/shop/update/phone")
public class ShopUpdatePhoneController extends MerchantBaseController<ReqShopUpdatePhone,EnumRespCode> {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    MerchantInfoManager infoManager;
    @Autowired
    private TokenServiceInterface tokenServiceInterface;
    @Autowired
    private SMSServiceInterface smsService;


    @Override
    protected EnumRespCode doService(String userId, ReqShopUpdatePhone req) throws MerchantClientException {
        // 1.校验凭据
    /*    boolean checkResult = tokenServiceInterface.checkCredential(req.getCredential());
        if (!checkResult){
            throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_RESET_CREDENTIAL_INVALID);
        }*/
        //校验验证码是否正确
        boolean result = smsService.verifyCode(req.getNewPhone(), EnumCodeType.modify_phone, req.getVerifyCode());
        if (!result) {
            throw new MerchantClientException(EnumRespCode.VERIFY_CODE_FAIL);
        }
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (entity==null){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        entity.setPhone(req.getNewPhone());
        boolean b = merchantInfoService.merchantUpdate(entity);
        if (b){
            return EnumRespCode.SUCCESS;
        }else {
            throw new MerchantClientException(EnumRespCode.FAIL);
        }
    }
}
