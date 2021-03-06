package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantInfoManager;
import com.xinguang.tubobo.impl.merchant.service.MerchantMessageSettingsService;
import com.xinguang.tubobo.merchant.api.enums.EnumIdentifyType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.merchant.web.request.shop.ShopIdentifyRequest;
import com.xinguang.tubobo.merchant.web.response.MerchantInfoResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/13.
 */
@Controller
@RequestMapping(value = "/shop/identify")
public class ShopIdentifyController extends MerchantBaseController<ShopIdentifyRequest,MerchantInfoResponse> {
    @Autowired
    private MerchantInfoManager merchantInfoManager;
    @Autowired
    MerchantMessageSettingsService settingsService;
    @Autowired
    private Config config;

    @Override
    protected MerchantInfoResponse doService(String userId, ShopIdentifyRequest req) throws MerchantClientException {
//        if (config.isSysInterfaceCloseFlag()){
//            throw new MerchantClientException(EnumRespCode.INTERFACE_NOT_SUPPORT);
//        }
        logger.info("收到店铺申请请求 ：{}，",req.toString() );
        MerchantInfoEntity infoEntity = new MerchantInfoEntity();
        BeanUtils.copyProperties(req,infoEntity);
        MerchantInfoEntity respEntity = merchantInfoManager.identify(userId,req.getPayPassword(), EnumIdentifyType.MERCHANT.getValue(),infoEntity);
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

    /**
     * 请求参数和实体参数转化
     * @param userId
     * @param shopIdentifyRequest
     * @return
     */
    private MerchantInfoEntity translateRequestToEntity(String userId,ShopIdentifyRequest shopIdentifyRequest){
        MerchantInfoEntity merchant = new MerchantInfoEntity();
        BeanUtils.copyProperties(shopIdentifyRequest,merchant);
        merchant.setUserId(userId);
        merchant.setIdentifyType(EnumIdentifyType.MERCHANT.getValue());
        return merchant;
    }

    @Override
    protected boolean needIdentify() {
        return false;
    }
}
