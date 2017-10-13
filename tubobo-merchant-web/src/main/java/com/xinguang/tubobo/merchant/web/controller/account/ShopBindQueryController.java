package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.RespShopBindQuery;
import com.xinguang.tubobo.postHouse.api.dto.ProviderInfoDTO;
import com.xinguang.tubobo.postHouse.api.service.ProviderServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lvhantai on 2017/10/11.
 * 查询商家绑定情况
 */
@Controller
@RequestMapping("/query/postBind")
public class ShopBindQueryController extends MerchantBaseController<Object, RespShopBindQuery>{

    @Autowired private ProviderServiceInterface providerServiceInterface;

    @Autowired private MerchantInfoService merchantInfoService;

    @Override
    protected RespShopBindQuery doService(String userId, Object req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = merchantInfoService.findByUserId(userId);
        ProviderInfoDTO providerInfoDTO = providerServiceInterface.getProviderInfoById(infoEntity.getProviderId());
        RespShopBindQuery respShopBindQuery = new RespShopBindQuery();
        respShopBindQuery.setBindStatus(infoEntity.getBindStatus());
        respShopBindQuery.setOperatorName(providerInfoDTO.getOperatorName());
        respShopBindQuery.setOperatorPhone(providerInfoDTO.getOperatorPhone());
        respShopBindQuery.setProviderId(providerInfoDTO.getId());
        respShopBindQuery.setProviderName(providerInfoDTO.getName());
        return respShopBindQuery;
    }
}
