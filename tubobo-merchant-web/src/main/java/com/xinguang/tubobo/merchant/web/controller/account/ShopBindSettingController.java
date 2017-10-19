package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.api.enums.EnumAuthentication;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqBindSetting;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.postHouse.api.service.BindToMerchantServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lvhantai on 2017/10/11.
 */
@Controller
@RequestMapping("/setting/postBind")
@Transactional
public class ShopBindSettingController extends MerchantBaseController<ReqBindSetting, Object>{
    @Autowired private MerchantInfoService merchantInfoService;
    @Autowired private BindToMerchantServiceInterface merchantServiceInterface;
    @Override
    protected Object doService(String userId, ReqBindSetting req) throws MerchantClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if(!EnumAuthentication.SUCCESS.getValue().equals(entity.getMerchantStatus())){
            throw new MerchantClientException(EnumRespCode.SHOP_NOT_AUTHED);
        }
//        if(entity.getProviderId() != req.getProviderId()){
//            throw new MerchantClientException(EnumRespCode.SHOP_PROVIDERID_DIFFER);
//        }
        boolean result;
        if(req.isAgreeBind()){
            result = merchantInfoService.bindProviderAgree(userId, req.getProviderId());
            if(result){
                result = merchantServiceInterface.bindProviderAgree(userId, req.getProviderId());
            }
        }else {
            result = merchantServiceInterface.bindProviderReject(userId, req.getProviderId());
        }
        if(result){
            return "";
        }
        return EnumRespCode.FAIL.getValue();
    }
}
