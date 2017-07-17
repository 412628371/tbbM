package com.xinguang.tubobo.merchant.web.controller.third;

import com.xinguang.tubobo.impl.merchant.service.MerchantThirdBindService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.third.ReqThirdUnbind;
import com.xinguang.tubobo.merchant.web.response.third.RespThirdUnbind;
import com.xinguang.tubobo.notify.api.TbbNotifyService;
import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by xuqinghua on 2017/7/14.
 */
@Controller
@RequestMapping("third/unbindStore")
public class ThirdUnbindController extends MerchantBaseController<ReqThirdUnbind,RespThirdUnbind> {
    Logger logger = LoggerFactory.getLogger(ThirdUnbindController.class);
    @Autowired
    private TbbNotifyService tbbNotifyService;
    @Autowired private MerchantThirdBindService thirdBindService;
    @Override
    protected RespThirdUnbind doService(String userId, ReqThirdUnbind req) throws MerchantClientException {
        TakeoutNotifyConstant.PlatformCode platform;
        if (TakeoutNotifyConstant.PlatformCode.ELE.getValue().equals(req.getPlatformCode())){
            platform = TakeoutNotifyConstant.PlatformCode.ELE;
        }else if (TakeoutNotifyConstant.PlatformCode.YZ.getValue().equals(req.getPlatformCode())){
            platform = TakeoutNotifyConstant.PlatformCode.YZ;
        }else {
            throw new MerchantClientException(EnumRespCode.PARAMS_ERROR);
        }
        try {
            tbbNotifyService.unBind(req.getPlatformCode(),userId);
            thirdBindService.unbindMt(platform,userId);
        } catch (Exception e) {
            logger.error("第三方平台解绑异常：platformCode:{}",req.getPlatformCode(),e);
            throw new MerchantClientException(EnumRespCode.FAIL);
        }
        RespThirdUnbind respThirdUnbind = new RespThirdUnbind();
        respThirdUnbind.setPlatformCode(req.getPlatformCode());
        respThirdUnbind.setResult(true);
        return respThirdUnbind;
    }
}
