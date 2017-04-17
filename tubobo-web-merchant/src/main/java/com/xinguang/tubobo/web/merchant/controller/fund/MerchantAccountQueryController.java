package com.xinguang.tubobo.web.merchant.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.response.AccountInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.api.ClientException;
import com.xinguang.tubobo.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.rider.entity.RiderInfoEntity;
import com.xinguang.tubobo.web.merchant.MerchantBaseController;
import com.xinguang.tubobo.web.merchant.response.ResAccountInfo;
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
	protected ResAccountInfo doService(String userId, Object req) throws ClientException {
		MerchantInfoEntity entity = merchantInfoService.findByUserId(userId);
		if (entity == null){
			throw new ClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}

		TbbAccountResponse<AccountInfo> response = tbbAccountService.getAccountInfo(entity.getAccountId());
		if (response != null && response.isSucceeded()){
			ResAccountInfo res = new ResAccountInfo();
			BeanUtils.copyProperties(response.getData(),res);
			return res;
		}else{
			logger.error("查询资金账户出错，userId: "+userId+" accountId: "+entity.getAccountId());
			throw new ClientException(EnumRespCode.ACCOUNT_INFO_NOT_EXIST);
		}
	}
}
