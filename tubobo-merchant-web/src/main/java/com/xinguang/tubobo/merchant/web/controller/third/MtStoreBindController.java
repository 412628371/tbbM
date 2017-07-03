package com.xinguang.tubobo.merchant.web.controller.third;

import com.xinguang.tubobo.TbbNotifyMtStoreMapDTO;
import com.xinguang.tubobo.impl.merchant.entity.MerchantThirdBindEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantThirdBindService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.third.RespMtStoreBind;
import com.xinguang.tubobo.notify.api.TbbNotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuqinghua on 2017/6/30.
 */
@Controller
@RequestMapping("third/mt/getStoreBindUrl")
public class MtStoreBindController extends MerchantBaseController<Object,RespMtStoreBind> {
    @Autowired private TbbNotifyService tbbNotifyService;
    @Autowired private MerchantThirdBindService thirdBindService;
    @Override
    protected RespMtStoreBind doService(String userId, Object req) throws MerchantClientException {
        MerchantThirdBindEntity entity = thirdBindService.getByUserId(userId);
        String authToken = "";
        if (null!=entity){
            authToken = entity.getMtAuthToken();
        }
        TbbNotifyMtStoreMapDTO storeMapDTO = tbbNotifyService.getMtStoreBindRequestUrl(userId,authToken);
        RespMtStoreBind respMtStoreBind = new RespMtStoreBind();
        respMtStoreBind.setMtStoreBindUrl(storeMapDTO.getMtStoreBindUrl());
        respMtStoreBind.setMtStoreReleaseBindingUrl(storeMapDTO.getMtStoreReleaseBindingUrl());
        return respMtStoreBind;
    }
}
