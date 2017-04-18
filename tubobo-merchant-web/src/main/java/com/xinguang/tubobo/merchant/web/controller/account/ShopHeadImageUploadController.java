package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.web.request.ShopHeadImageUploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/info/upload/headImage")
public class ShopHeadImageUploadController extends MerchantBaseController<ShopHeadImageUploadRequest,EnumRespCode> {

    @Autowired
    MerchantInfoService merchantInfoService;
    @Override
    protected EnumRespCode doService(String userId, ShopHeadImageUploadRequest req) throws MerchantClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity){
            return EnumRespCode.MERCHANT_NOT_EXISTS;
        }
        merchantInfoService.updateHeadImage(userId,req.getAvatarUrl());
        return EnumRespCode.SUCCESS;
    }
}
