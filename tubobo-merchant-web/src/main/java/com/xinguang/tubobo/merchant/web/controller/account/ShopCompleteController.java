package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantInfoManager;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.v2.ReqShopComplete;
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
    @Override
    protected MerchantInfoResponse doService(String userId, ReqShopComplete req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = new MerchantInfoEntity();
        BeanUtils.copyProperties(req,infoEntity);
        MerchantInfoEntity respEntity = infoManager.shopComplete(userId,infoEntity);
        MerchantInfoResponse infoResponse = new MerchantInfoResponse();
        BeanUtils.copyProperties(respEntity,infoResponse);
        return infoResponse;
    }
}
