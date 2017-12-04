package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.manager.MerchantInfoManager;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumIdentifyType;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.ReqUniquePersonIdentify;
import com.xinguang.tubobo.merchant.web.request.shop.v2.ReqPersonIdentify;
import com.xinguang.tubobo.merchant.web.response.MerchantInfoResponse;
import com.xinguang.tubobo.rider.api.RiderToAdminServiceInterface;
import com.xinguang.tubobo.rider.api.dto.RiderInfoDTO;
import com.xinguang.tubobo.rider.api.enums.EnumRiderAuthentication;
import com.xinguang.xingchen.SMSplatform.enums.EnumCodeType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询身份证是否允许认证(认证骑手就不可认证商家)
 */
@RestController
@RequestMapping(value = "/idCardNo/status")
public class UniqueIdentifyController extends MerchantBaseController<ReqUniquePersonIdentify,String> {
    @Autowired
    private MerchantInfoManager merchantInfoManager;
    @Override
    protected String doService(String userId, ReqUniquePersonIdentify req) throws MerchantClientException {
        boolean b = merchantInfoManager.checkByIdCardIfRider(req.getIdCardNo(),userId);
        if (b){
            throw new MerchantClientException(EnumRespCode.SUCCESS);
        }else{
            logger.error("店铺申请失败，该用户已经申请成为骑手。IdcardNo:{}",req.getIdCardNo());
            throw new MerchantClientException(EnumRespCode.SHOP_ALREADY_BOUND_RIDER);
        }
    }


}
