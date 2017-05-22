package com.xinguang.tubobo.merchant.web.controller.push;

import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqPushSettings;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/4/15.
 */
@Controller
@RequestMapping("/settings/push")
public class PushSettingsController extends MerchantBaseController<ReqPushSettings,EnumRespCode> {
    @Autowired
    MerchantSettingsService settingsService;
    @Override
    protected EnumRespCode doService(String userId, ReqPushSettings req) throws MerchantClientException {
        MerchantSettingsEntity entity = new MerchantSettingsEntity();
        entity.setUserId(userId);
        if (null != req.getPushMsgOrderGrabed()){
            entity.setPushMsgOrderGrabed(req.getPushMsgOrderGrabed());
        }
        if (null!=req.getPushMsgOrderFinished()){
            entity.setPushMsgOrderFinished(req.getPushMsgOrderFinished());
        }
        if (null != req.getPushMsgOrderExpired()){
            entity.setPushMsgOrderExpired(req.getPushMsgOrderExpired());
        }
//        if (null != req.getDeviceToken()){
//            entity.setDeviceToken(req.getDeviceToken());
//        }

        Boolean result = settingsService.updateSettings(userId,entity);
        if (!result){
            throw new MerchantClientException(EnumRespCode.MERCHANT_PUSH_SETTINGS_FAILURE);
        }
        return EnumRespCode.SUCCESS;
    }
}
