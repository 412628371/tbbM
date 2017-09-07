package com.xinguang.tubobo.impl.merchant.manager;

import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantToPostHouseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xuqinghua on 2017/9/1.
 */
public class MerchantToPostHouseServiceImpl implements MerchantToPostHouseServiceInterface {

    @Autowired private MerchantInfoService merchantInfoService;
    @Override
    public boolean bindProvider(String userId, Long providerId, String providerName) {
        boolean result = merchantInfoService.bindProvider(userId,providerId,providerName);
        return result;
    }
}
