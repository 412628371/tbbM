package com.xinguang.tubobo.web.merchant.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import com.xinguang.tubobo.impl.rider.service.RiderInfoService;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ReqAccountResetPwd;
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
	protected EnumRespCode doService(String userId, ReqAccountResetPwd req) throws ClientException {
		MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
		if (entity == null){
			throw new ClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}
		TbbAccountResponse<Boolean> response = tbbAccountService.updatePayPassword(entity.getAccountId(), req.getNewPwd(), req.getOldPwd());
		if (response != null && response.isSucceeded()){
			return EnumRespCode.SUCCESS;
		} else {
			return EnumRespCode.FAIL;
		}
	}
}
