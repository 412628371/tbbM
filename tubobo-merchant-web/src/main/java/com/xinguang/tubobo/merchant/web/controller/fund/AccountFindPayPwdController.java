package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.AESUtils;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqAccountResetPwd;
import com.xinguang.usercentergate.api.TokenServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2017/5/17.
 */
@Controller
@RequestMapping(value = "/account/payPwd/reset")
public class AccountFindPayPwdController extends MerchantBaseController<ReqAccountResetPwd,EnumRespCode> {
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    private TokenServiceInterface tokenServiceInterface;
    @Override
    protected EnumRespCode doService(String userId, ReqAccountResetPwd req) throws MerchantClientException {
       logger.info("重置支付密码请求：userID：{}，req:{}",userId,req.toString());
        MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);

        // 1.校验凭据
        boolean checkResult = tokenServiceInterface.checkCredential(req.getCredential());
        if (!checkResult){
            throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_RESET_CREDENTIAL_INVALID);
        }
        //2. 重置支付密码
        String plainPwd = AESUtils.decrypt(req.getPayPassword());
        TbbAccountResponse<Boolean> response = tbbAccountService.resetPayPassword(entity.getAccountId(),plainPwd);
        if(response != null && response.isSucceeded() && response.getData()){
            logger.info("重置支付密码成功：userID：{}",userId);
            if (!entity.getHasSetPayPwd()){
                merchantInfoService.modifyPwdSetFlag(userId);
            }
            return EnumRespCode.SUCCESS;
        }
        logger.info("重置支付密码失败：userID：{}，errorCode:{},errorMsg:{}",
                userId,response.getErrorCode(),response.getMessage());
        throw new MerchantClientException(EnumRespCode.FAIL);
    }

    @Override
    protected boolean needIdentify() {
        return true;
    }
}
