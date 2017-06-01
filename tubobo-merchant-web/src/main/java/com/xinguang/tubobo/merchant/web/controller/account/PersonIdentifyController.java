package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantInfoManager;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumIdentifyType;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.v2.ReqPersonIdentify;
import com.xinguang.tubobo.merchant.web.response.MerchantInfoResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 货主认证
 */
@Controller
@RequestMapping(value = "/info/person/identify")
public class PersonIdentifyController extends MerchantBaseController<ReqPersonIdentify,MerchantInfoResponse> {
    @Autowired
    private MerchantInfoManager merchantInfoManager;
    @Override
    protected MerchantInfoResponse doService(String userId, ReqPersonIdentify req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = new MerchantInfoEntity();
        BeanUtils.copyProperties(req,infoEntity);
        MerchantInfoEntity respEntity = merchantInfoManager.identify(userId,req.getPayPassword(), EnumIdentifyType.CONSIGNOR.getValue(),infoEntity);
        MerchantInfoResponse infoResponse = new MerchantInfoResponse();
        BeanUtils.copyProperties(respEntity,infoResponse);
        return infoResponse;
    }
}
