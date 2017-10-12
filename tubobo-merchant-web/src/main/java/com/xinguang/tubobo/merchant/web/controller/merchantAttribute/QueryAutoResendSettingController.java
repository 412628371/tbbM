package com.xinguang.tubobo.merchant.web.controller.merchantAttribute;

import com.xinguang.tubobo.impl.merchant.entity.AutoResendPostSettingEntity;
import com.xinguang.tubobo.impl.merchant.service.AutoResendPostSettingsService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.shop.ReqAutoPostOrderOperate;
import com.xinguang.tubobo.merchant.web.response.merchantConfig.RespAutoResendPostOrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by yanx on 2017/9/17.
 * 查询驿站自动发单开关接口
 */
@Controller
@RequestMapping(value = "/query/autoPostOrder")
public class QueryAutoResendSettingController extends MerchantBaseController<String,RespAutoResendPostOrderSetting> {
    @Autowired
    AutoResendPostSettingsService settingsService;

    @Override
    protected RespAutoResendPostOrderSetting doService(String userId, String req) throws MerchantClientException {

        RespAutoResendPostOrderSetting resp = new RespAutoResendPostOrderSetting();
        AutoResendPostSettingEntity entity = settingsService.findBuUserId(userId);
        if (null!=entity){
            resp.setAutoPostOrder(entity.getAutoPostOrderResendOpen());
        }
        return resp;
    }
}
