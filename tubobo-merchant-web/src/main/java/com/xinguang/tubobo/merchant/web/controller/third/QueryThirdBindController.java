package com.xinguang.tubobo.merchant.web.controller.third;

import com.xinguang.tubobo.impl.merchant.entity.MerchantThirdBindEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantThirdBindService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.third.RespQueryStoreBind;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuqinghua on 2017/6/30.
 */
@Controller
@RequestMapping("third/getBindStatus")
public class QueryThirdBindController extends MerchantBaseController<Object,RespQueryStoreBind> {
    @Autowired
    private MerchantThirdBindService thirdBindService;
    @Override
    protected RespQueryStoreBind doService(String userId, Object req) throws MerchantClientException {
        MerchantThirdBindEntity entity = thirdBindService.getByUserId(userId);
        RespQueryStoreBind respQueryStoreBind = new RespQueryStoreBind();
        if (null == entity){
            respQueryStoreBind.setEleBound(false);
            respQueryStoreBind.setMtBound(false);
            respQueryStoreBind.setYzBound(false);
        }else {
            BeanUtils.copyProperties(entity,respQueryStoreBind);
        }
        return respQueryStoreBind;
    }
}
