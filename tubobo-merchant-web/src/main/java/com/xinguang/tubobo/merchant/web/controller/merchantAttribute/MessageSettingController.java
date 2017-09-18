package com.xinguang.tubobo.merchant.web.controller.merchantAttribute;

import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantMessageSettingsService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.ReqMessageOperate;
import com.xinguang.tubobo.merchant.web.request.shop.ReqPushSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * Created by yanx on 2017/9/17.
 * 短信开关接口(查询在merchantInfo中封装)
 */
@Controller
@RequestMapping(value = "/settings/message")
public class MessageSettingController extends MerchantBaseController<ReqMessageOperate,EnumRespCode> {
    @Autowired
    MerchantMessageSettingsService settingsService;

    @Override
    protected EnumRespCode doService(String userId, ReqMessageOperate req) throws MerchantClientException {

        MerchantMessageSettingsEntity entity = new MerchantMessageSettingsEntity();
        entity.setUserId(userId);
        entity.setMessageOpen(req.getMessageOpen());
        Boolean result = settingsService.updateSettings(userId,entity);
        if (!result){
            throw new MerchantClientException(EnumRespCode.	MERCHANT_MESSAGE_PUSH_SETTINGS_FAILURE
                    );
        }
        return EnumRespCode.SUCCESS;

    }
}
