package com.xinguang.tubobo.merchant.web.controller.marketing;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.common.DateUtils;
import com.xinguang.tubobo.impl.merchant.disconf.Config;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.marketing.ReqBDCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by lvhantai on 2017/6/21.
 * 添加BD邀请码
 */
@Controller
@RequestMapping(value = "/marketing/bd")
public class BDCodeInsertController extends MerchantBaseController<ReqBDCode, Object>{
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private Config config;

    @Override
    protected Object doService(String userId, ReqBDCode req) throws MerchantClientException {
        MerchantInfoEntity merchantInfoEntity = merchantInfoService.findByUserId(userId);
        //读取限制时间
        Double timeLimit = config.getBdcodeBindTimeliness();
        Date createDate = merchantInfoEntity.getCreateDate();
        if(null != createDate){
            double timeDiff = DateUtils.countHourBetweenTwoDate(createDate, new Date());
            if(timeDiff>timeLimit){
                throw new MerchantClientException(EnumRespCode.MERCHANT_BDCODE_TIMEOUT);
            }
        }
        if(StringUtils.isNotBlank(merchantInfoEntity.getBdCode())){
            throw new MerchantClientException(EnumRespCode.MERCHANT_BDCODE_ERROR);
        }
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
