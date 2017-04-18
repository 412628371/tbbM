package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.web.request.ReqAccountResetPwd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/account/resetPwd")
public class MerchantAccountResetPwdController extends MerchantBaseController<ReqAccountResetPwd,EnumRespCode> {

	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private TbbAccountService tbbAccountService;

	@Override
	protected EnumRespCode doService(String userId, ReqAccountResetPwd req) throws MerchantClientException {
		MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
		if (entity == null){
			throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}
		TbbAccountResponse<Boolean> response = tbbAccountService.updatePayPassword(entity.getAccountId(), req.getNewPwd(), req.getOldPwd());
		if (response != null && response.isSucceeded()){
			return EnumRespCode.SUCCESS;
		} else {
			return EnumRespCode.FAIL;
		}
	}
}
