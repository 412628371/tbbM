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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 查询身份证是否允许认证(认证骑手就不可认证商家)
 */
@Controller
@RequestMapping(value = "/idCardNo/status")
public class UniqueIdentifyController extends MerchantBaseController<ReqUniquePersonIdentify,String> {
    @Autowired
    private MerchantInfoManager merchantInfoManager;
    @Override
    protected String doService(String userId, ReqUniquePersonIdentify req) throws MerchantClientException {
        return merchantInfoManager.checkByIdCardIfRider(req.getIdCardNo());
    }


}
