package com.xinguang.tubobo.merchant.web.controller.account;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.v2.ReqPersonIdentify;
import com.xinguang.tubobo.merchant.web.response.MerchantInfoResponse;
import com.xinguang.tubobo.rider.api.RiderToAdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/5/25.
 */
public class PersonIdentifyController extends MerchantBaseController<ReqPersonIdentify,MerchantInfoResponse> {
    @Autowired
    RiderToAdminServiceInterface riderToAdminServiceInterface;

    @Autowired
    TbbAccountService tbbAccountService;
    @Override
    protected MerchantInfoResponse doService(String userId, ReqPersonIdentify req) throws MerchantClientException {
        return null;
    }
}
