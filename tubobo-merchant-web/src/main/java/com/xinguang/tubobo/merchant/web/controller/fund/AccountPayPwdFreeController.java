package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.cache.RedisOp;
import com.xinguang.tubobo.impl.merchant.common.AESUtils;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqPayPwdFree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/5/17.
 */
@Controller
@RequestMapping("/account/pwdFree/set")
public class AccountPayPwdFreeController extends MerchantBaseController<ReqPayPwdFree,Object> {
    @Autowired
    MerchantInfoService infoService;
    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    RedisOp redisOp;
    @Override
    protected Object doService(String userId, ReqPayPwdFree req) throws MerchantClientException {
        MerchantInfoEntity infoEntity = infoService.findByUserId(userId);
        if (req.getEnablePwdFree()){
            redisOp.checkPwdErrorTimes(MerchantConstants.KEY_PWD_WRONG_TIMES_FREE,userId);
            //TODO 验证支付密码，错误计数？
            TbbAccountResponse<Boolean> response = tbbAccountService.checkPayPassword(infoEntity.getAccountId(),
                    AESUtils.decrypt(req.getPayPassword()));
            if (null == response || !response.isSucceeded()){
                throw new MerchantClientException(EnumRespCode.FAIL);
            }else {
                if (!response.getData()){
                    redisOp.increment(MerchantConstants.KEY_PWD_WRONG_TIMES_FREE,userId,1);
                    throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_ERROR,
                            String.valueOf(redisOp.getAvailableWrongTimes(MerchantConstants.KEY_PWD_WRONG_TIMES_FREE,userId)));
                }
            }
            redisOp.resetPwdErrorTimes(userId);
        }
        infoService.freePayPwdSet(userId,req.getEnablePwdFree());
        return null;
    }

    @Override
    protected boolean needIdentify() {
        return true;
    }
}
