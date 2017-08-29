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
import com.xinguang.tubobo.merchant.web.request.ReqAccountFirstCreatePwd;
import com.xinguang.tubobo.merchant.web.request.ReqAccountModifyPwd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/account/payPwd/firstCreate")
public class AccountFirstCreatePwdController extends MerchantBaseController<ReqAccountFirstCreatePwd,EnumRespCode> {

	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private TbbAccountService tbbAccountService;
	@Autowired
	RedisOp redisOp;

	@Override
	protected EnumRespCode doService(String userId, ReqAccountFirstCreatePwd req) throws MerchantClientException {
		logger.info("收到首次设置密码请求：userId:{}",userId);
		MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
		if (entity == null){
			throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}
		//2. 重置支付密码
		String password = AESUtils.decrypt(req.getPassword());
		boolean flag=entity.getHasSetPayPwd();
		if (flag){
			logger.info("该账户已经完成第一次密码设置,1.42 permit：userID：{}",
					userId);
			//throw new MerchantClientException(EnumRespCode.PASSWORD_FIRSTCREATE_FAIL);
		}
		TbbAccountResponse<Boolean> response = tbbAccountService.resetPayPassword(entity.getAccountId(),password);
		if(response != null && response.isSucceeded() && response.getData()){
			logger.info("设置支付密码成功：userID：{}",userId);
			if (!entity.getHasSetPayPwd()){
				merchantInfoService.modifyPwdSetFlag(userId);
			}
			redisOp.resetPwdErrorTimes(userId);
			return EnumRespCode.SUCCESS;
		}
		logger.info("设置支付密码失败：userID：{}，errorCode:{},errorMsg:{}",
				userId,response.getErrorCode(),response.getMessage());
		throw new MerchantClientException(EnumRespCode.FAIL);
	}

	@Override
	protected boolean needIdentify() {
		return true;
	}
}
