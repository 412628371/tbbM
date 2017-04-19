package com.xinguang.tubobo.merchant.web.controller.push;

import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantSettingsService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.response.RespSettingsQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/19.
 */
@RequestMapping("/settings/push/query")
@Controller
public class PushSettingsQueryController extends MerchantBaseController<Object,RespSettingsQuery>{

    @Autowired
    MerchantSettingsService settingsService;
    @Override
    protected RespSettingsQuery doService(String userId, Object req) throws MerchantClientException {
        MerchantSettingsEntity entity = settingsService.findBuUserId(userId);
        RespSettingsQuery respSettingsQuery = new RespSettingsQuery();
        if (entity == null){
            entity = new MerchantSettingsEntity();
            entity.setPushMsgOrderExpired(true);
            entity.setPushMsgOrderFinished(true);
            entity.setPushMsgOrderGrabed(true);
        }
        BeanUtils.copyProperties(entity,respSettingsQuery);
        return respSettingsQuery;
    }
}
