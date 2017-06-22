package com.xinguang.tubobo.merchant.web.controller.marketing;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.marketing.ReqBDCode;
import com.xinguang.tubobo.merchant.web.response.marketing.RespBDCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lvhantai on 2017/6/21.
 * 添加BD邀请码
 */
@Controller
@RequestMapping(value = "/marketing/bd")
public class BDCodeInsertController extends MerchantBaseController<ReqBDCode, Object>{
    @Autowired
    MerchantInfoService merchantInfoService;

    @Override
    protected Object doService(String userId, ReqBDCode req) throws MerchantClientException {
        String code = req.getBdCode();
        if(code.length()>20){
           code = code.substring(0, 20);
        }
        int result = merchantInfoService.updateDBCode(userId, code);
        if(result != 1){
            throw new MerchantClientException(EnumRespCode.FAIL);
        }
        return result;
    }
}
