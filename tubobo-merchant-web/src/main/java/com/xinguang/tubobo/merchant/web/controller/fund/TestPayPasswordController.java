package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/21.
 */
@Controller
@RequestMapping("/test/resetpwd")
public class TestPayPasswordController extends MerchantBaseController<TestPayPasswordReq,Object> {
    @Autowired
    TbbAccountService tbbAccountService;
    @Override
    protected Object doService(String userId, TestPayPasswordReq req) throws MerchantClientException {
        TbbAccountResponse<Boolean> response= tbbAccountService.resetPayPassword(req.getAccountId(),"111111");
        return response.getData().booleanValue();
    }

    @Override
    protected boolean needLogin() {
        return false;
    }
}
