package com.xinguang.tubobo.merchant.web;

import com.xinguang.tubobo.impl.merchant.manager.MerchantToPostHouseServiceImpl;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantPostExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    MerchantToPostHouseServiceImpl postHouseService;

    @RequestMapping("unbind/{userId}/{providerId}")
    @ResponseBody
    public EnumMerchantPostExceptionCode test(@PathVariable String userId,@PathVariable long providerId){
        EnumMerchantPostExceptionCode code = postHouseService.unbindProvider(userId,providerId);
        return code;
    }
}
