package com.xinguang.tubobo.merchant.web.controller.account;

import com.hzmux.hzcms.common.utils.AliOss;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantInfoManager;
import com.xinguang.tubobo.impl.merchant.service.MerchantMessageSettingsService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.v2.ReqShopComplete;
import com.xinguang.tubobo.merchant.web.response.MerchantInfoResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 完善店铺信息.
 */
@Controller
@RequestMapping(value = "/info/shop/complete")
public class ShopCompleteController extends MerchantBaseController<ReqShopComplete,MerchantInfoResponse> {
    @Autowired
    MerchantInfoManager infoManager;
    @Autowired
    MerchantMessageSettingsService settingsService;
    @Autowired
    private Config config;
    @Override
    protected MerchantInfoResponse doService(String userId, ReqShopComplete req) throws MerchantClientException {
//        if (config.isSysInterfaceCloseFlag()){
//            throw new MerchantClientException(EnumRespCode.INTERFACE_NOT_SUPPORT);
//        }
        MerchantInfoEntity infoEntity = new MerchantInfoEntity();
        BeanUtils.copyProperties(req,infoEntity);
        MerchantInfoEntity respEntity = infoManager.shopComplete(userId,infoEntity);
        AliOss.generateMerchantSignedUrl(respEntity);
        MerchantInfoResponse infoResponse = new MerchantInfoResponse();
        BeanUtils.copyProperties(respEntity,infoResponse);
        //封装messageOpen
        MerchantMessageSettingsEntity entity = settingsService.findByUserIdAndCreate(respEntity.getUserId());
        if (entity == null){
            entity = new MerchantMessageSettingsEntity();
            entity.setMessageOpen(false);
        }
        infoResponse.setMessageOpen(entity.getMessageOpen());
        return infoResponse;
    }
}
