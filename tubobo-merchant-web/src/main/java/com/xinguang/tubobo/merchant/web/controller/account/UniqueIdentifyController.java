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

import java.util.List;

/**
 * 查询身份证是否允许认证(认证骑手就不可认证商家)
 */
@RestController
@RequestMapping(value = "/idCardNo/status")
public class UniqueIdentifyController extends MerchantBaseController<ReqUniquePersonIdentify,String> {
    @Autowired
    private RiderToAdminServiceInterface riderToAdminServiceInterface;
    @Override
    protected String doService(String userId, ReqUniquePersonIdentify req) throws MerchantClientException {
        List<RiderInfoDTO> riderInfo = riderToAdminServiceInterface.findByIdCardNoAndUserId(req.getIdCardNo(), null);
        if (riderInfo!=null&&riderInfo.size()>0){
            logger.warn("店铺申请失败，改身份证号已注册成为骑手。IdcardNo:{}",req.getIdCardNo());
            throw new MerchantClientException(EnumRespCode.SHOP_ALREADY_BOUND_RIDER);
        }else{
            throw new MerchantClientException(EnumRespCode.SUCCESS);

        }
    }


}
