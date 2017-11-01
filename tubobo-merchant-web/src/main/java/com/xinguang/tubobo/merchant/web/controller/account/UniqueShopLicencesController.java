package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.ReqUniqueLicences;
import com.xinguang.tubobo.merchant.web.request.shop.ReqUniquePersonIdentify;
import com.xinguang.tubobo.rider.api.RiderToAdminServiceInterface;
import com.xinguang.tubobo.rider.api.dto.RiderInfoDTO;
import com.xinguang.tubobo.rider.api.enums.EnumRiderAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 查询营业执照是否重复
 */
@Controller
@RequestMapping(value = "/licences/uniqueQuery")
public class UniqueShopLicencesController extends MerchantBaseController<ReqUniqueLicences,String> {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Override
    protected String doService(String userId, ReqUniqueLicences req) throws MerchantClientException {

        MerchantInfoEntity merchantInfoEntity = merchantInfoService.findByLicencesNo(req.getShopLicencesNo());

        //判断
        boolean legalForVerify =true;
        if (merchantInfoEntity!=null){
            //重复
            throw new MerchantClientException(EnumRespCode.SHOP_LICENCESNO_EXIST);
        }else {
            throw new MerchantClientException(EnumRespCode.SUCCESS);
        }

    }
}
