package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.AccountInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.web.response.ResAccountInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/account/query")
public class MerchantAccountQueryController extends MerchantBaseController<Object,ResAccountInfo> {

	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private TbbAccountService tbbAccountService;

	@Override
	protected ResAccountInfo doService(String userId, Object req) throws MerchantClientException {
		MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
		if (entity == null){
			throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}

		TbbAccountResponse<AccountInfo> response = tbbAccountService.getAccountInfo(entity.getAccountId());
		if (response != null && response.isSucceeded()){
			AccountInfo accountInfo = response.getData();
			ResAccountInfo res = convertMoney(accountInfo);
			return res;
		}else{
			logger.error("查询资金账户出错，userId: "+userId+" accountId: "+entity.getAccountId());
			throw new MerchantClientException(EnumRespCode.ACCOUNT_INFO_NOT_EXIST);
		}
	}

	/**
	 * 金额转换
	 * @param accountInfo
	 * @return
     */
	public ResAccountInfo convertMoney(AccountInfo accountInfo){
		ResAccountInfo res = new ResAccountInfo();
		BeanUtils.copyProperties(accountInfo,res);
		res.setBalance(ConvertUtil.formatMoneyToString(accountInfo.getBalance()));
		res.setDeposit(ConvertUtil.formatMoneyToString(accountInfo.getDeposit()));
		res.setFrozen(ConvertUtil.formatMoneyToString(accountInfo.getFrozen()));
		return res;
	}

	@Override
	protected boolean needIdentify() {
		return true;
	}
}
