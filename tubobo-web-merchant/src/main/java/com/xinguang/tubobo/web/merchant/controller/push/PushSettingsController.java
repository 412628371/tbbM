package com.xinguang.tubobo.web.merchant.controller.push;

import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantSettingsService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ReqPushSettings;
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
    protected EnumRespCode doService(String userId, ReqPushSettings req) throws ClientException {
        MerchantSettingsEntity entity = new MerchantSettingsEntity(userId);
        entity.setPushMsgOrderGrabed(req.getPushMsgOrderGrabed());
        entity.setPushMsgOrderFinished(req.getPushMsgOrderFinished());
        entity.setPushMsgOrderExpired(req.getPushMsgOrderExpired());
        Boolean result = settingsService.updateSettings(entity);
        if (!result){
            throw new ClientException(EnumRespCode.MERCHANT_PUSH_SETTINGS_FAILURE);
        }
        return EnumRespCode.SUCCESS;
    }
}
