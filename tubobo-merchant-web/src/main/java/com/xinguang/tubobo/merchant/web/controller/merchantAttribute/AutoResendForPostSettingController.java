package com.xinguang.tubobo.merchant.web.controller.merchantAttribute;

import com.xinguang.tubobo.impl.merchant.entity.AutoResendPostSettingEntity;
import com.xinguang.tubobo.impl.merchant.service.AutoResendPostSettingsService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.ReqAutoPostOrderOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by yanx on 2017/9/17.
 * 驿站自动发单开关接口
 */
@Controller
@RequestMapping(value = "/settings/autoPostOrder")
public class AutoResendForPostSettingController extends MerchantBaseController<ReqAutoPostOrderOperate,EnumRespCode> {
    @Autowired
    AutoResendPostSettingsService settingsService;

    @Override
    protected EnumRespCode doService(String userId, ReqAutoPostOrderOperate req) throws MerchantClientException {

        AutoResendPostSettingEntity entity = new AutoResendPostSettingEntity();
        entity.setUserId(userId);
        entity.setAutoPostOrderResendOpen(req.getAutoPostOrder());
        Boolean result = settingsService.updateSettings(userId,entity);
        if (!result){
            throw new MerchantClientException(EnumRespCode.	MERCHANT_AUTO_RESEND_POST_SETTINGS_FAILURE
                    );
        }
        return EnumRespCode.SUCCESS;
    }
}
