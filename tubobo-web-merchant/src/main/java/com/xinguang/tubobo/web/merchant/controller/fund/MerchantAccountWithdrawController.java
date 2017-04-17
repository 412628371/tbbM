package com.xinguang.tubobo.web.merchant.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.TbbConstants;
import com.xinguang.tubobo.account.api.request.WithdrawConfirmRequest;
import com.xinguang.tubobo.account.api.request.WithdrawRequest;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.account.api.response.WithdrawInfo;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.request.ReqAccountWithdraw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/account/withdraw")
public class MerchantAccountWithdrawController extends MerchantBaseController<ReqAccountWithdraw,EnumRespCode> {

	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private TbbAccountService tbbAccountService;

	@Override
	protected EnumRespCode doService(String userId, ReqAccountWithdraw req) throws ClientException {
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(userId);
		if (merchant == null){
			throw new ClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}
		// 提现申请
		WithdrawRequest withdrawRequest = new WithdrawRequest(req.getAmount(), merchant.getAccountId(), TbbConstants.OperationTarget.BALANCE);
		TbbAccountResponse<WithdrawInfo> withdrawResponse = tbbAccountService.withdraw(withdrawRequest);
		if (withdrawResponse != null && withdrawResponse.isSucceeded()){
			// 提现申请确认
			WithdrawConfirmRequest withdrawConfirmRequest = WithdrawConfirmRequest.getInstanceOfConfirm(withdrawResponse.getData().getId(), "","");
			TbbAccountResponse<WithdrawInfo> withdrawConfirmResponse = tbbAccountService.withdrawConfirm(withdrawConfirmRequest);
			if (withdrawConfirmResponse != null && withdrawConfirmResponse.isSucceeded()){
				logger.info("withdraw confirm SUCCESS. merchantName:{}, merchantPhone:{}, accountId:{}, withdrawId:{}, amount:{}",
						merchant.getRealName(),merchant.getPhone(),merchant.getAccountId(),withdrawResponse.getData().getId(),req.getAmount());
				return EnumRespCode.SUCCESS;
			}else {
				logger.error("withdraw confirm FAIL. riderName:{}, riderPhone:{}, accountId:{}, withdrawId:{}, amount:{}, errorCode:{}, errorMsg{}",
						merchant.getRealName(),merchant.getPhone(),merchant.getAccountId(),withdrawResponse.getData().getId(),req.getAmount(),withdrawConfirmResponse.getErrorCode(),withdrawConfirmResponse.getMessage());
				return EnumRespCode.ACCOUNT_WITHDRAW_COMFIRM_FAIL;
			}
		} else {
			logger.error("withdraw apply FAIL. merchantName:{}, merchantPhone:{}, accountId:{}, amount:{}, errorCode:{}, errorMsg{}",
					merchant.getRealName(),merchant.getPhone(),merchant.getAccountId(),req.getAmount(),withdrawResponse.getErrorCode(),withdrawResponse.getMessage());
			return EnumRespCode.ACCOUNT_WITHDRAW_APPLY_FAIL;
		}
	}
}
