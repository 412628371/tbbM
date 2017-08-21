package com.xinguang.tubobo.merchant.web.controller.account;

import com.hzmux.hzcms.common.utils.AliOss;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.MerchantInfoResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/info/query")
public class QueryShopInfoController extends MerchantBaseController<Object,MerchantInfoResponse> {

    @Autowired
    MerchantInfoService merchantInfoService;
    @Autowired
    Config config;

    @Override
    protected MerchantInfoResponse doService(String userId, Object req) throws MerchantClientException {
        logger.info("店铺信息查询，userId: {}",userId);
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
        if (null == entity){
            throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
        }
        MerchantInfoResponse response = translateEntityToResponse(entity);
        return response;
    }

    private MerchantInfoResponse translateEntityToResponse(MerchantInfoEntity merchantInfoEntity){
        AliOss.generateMerchantSignedUrl(merchantInfoEntity);
        MerchantInfoResponse response= new MerchantInfoResponse();
        BeanUtils.copyProperties(merchantInfoEntity,response);
        response.setNonConfidentialPaymentLimit(config.getNonConfidentialPaymentLimit());
        return response;
    }

    @Override
    protected boolean needIdentify() {
        return false;
    }

}
