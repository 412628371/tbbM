package com.xinguang.tubobo.impl.merchant.manager;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.rider.api.RiderToAdminServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/5/26.
 */
@Service
public class MerchantInfoManager {
    @Autowired
    MerchantInfoService merchantInfoService;
    @Autowired
    RiderToAdminServiceInterface riderToAdminServiceInterface;

    @Autowired
    TbbAccountService tbbAccountService;

    public void identify(){

    }
}
