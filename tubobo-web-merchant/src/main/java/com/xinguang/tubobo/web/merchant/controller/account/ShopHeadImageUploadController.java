package com.xinguang.tubobo.web.merchant.controller.account;

import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ShopHeadImageUploadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/info/upload/headImage")
public class ShopHeadImageUploadController extends MerchantBaseController<ShopHeadImageUploadRequest,EnumRespCode>{

    @Autowired
    MerchantInfoService merchantInfoService;
    @Override
    protected EnumRespCode doService(String userId, ShopHeadImageUploadRequest req) throws ClientException {
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity){
            return EnumRespCode.MERCHANT_NOT_EXISTS;
        }
        merchantInfoService.updateHeadImage(userId,req.getAvatarUrl());
        return EnumRespCode.SUCCESS;
    }
}
