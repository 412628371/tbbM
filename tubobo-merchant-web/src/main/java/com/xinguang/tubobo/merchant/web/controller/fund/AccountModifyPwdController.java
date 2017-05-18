package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.AESUtils;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.web.request.ReqAccountModifyPwd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/account/payPwd/modify")
public class AccountModifyPwdController extends MerchantBaseController<ReqAccountModifyPwd,EnumRespCode> {

	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private TbbAccountService tbbAccountService;

	@Override
	protected EnumRespCode doService(String userId, ReqAccountModifyPwd req) throws MerchantClientException {
		logger.info("收到修改支付密码请求：userId:{}",userId);
		MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
		if (entity == null){
			throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}
		String plainNewPwd = AESUtils.decrypt(req.getNewPwd());
		String plainOldPwd = AESUtils.decrypt(req.getOldPwd());
		if (plainNewPwd.equals(plainOldPwd)){
			throw new MerchantClientException(EnumRespCode.ACCOUNT_PWD_CANT_EQUAL);
		}
		TbbAccountResponse<Boolean> response = tbbAccountService.updatePayPassword(entity.getAccountId(),
				plainNewPwd, plainOldPwd);
		if ( null == response){
			throw new MerchantClientException(EnumRespCode.FAIL);
		}
		if (response.isSucceeded() && response.getData()){
			return EnumRespCode.SUCCESS;
		} else {
			if (TbbAccountResponse.ErrorCode.ERROR_ACCOUNT_PAY_PWD_WRONG.getCode().
					equals(response.getErrorCode())){
				throw  new MerchantClientException(EnumRespCode.ACCOUNT_PWD_ERROR);
			}
			throw  new MerchantClientException(EnumRespCode.FAIL);
		}
	}
}
