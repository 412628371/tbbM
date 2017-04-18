package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.TbbConstants;
import com.xinguang.tubobo.account.api.TbbPage;
import com.xinguang.tubobo.account.api.request.AccountRecordsQueryCondition;
import com.xinguang.tubobo.account.api.response.AccountRecordInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqAccountTradeRecordList;
import com.xinguang.tubobo.merchant.web.response.ResAccountTradeRecord;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/account/tradeRecordList")
public class MerchantAccountTradeRecordListController extends MerchantBaseController<ReqAccountTradeRecordList,PageDTO<ResAccountTradeRecord>> {

	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private TbbAccountService tbbAccountService;

	@Override
	protected PageDTO<ResAccountTradeRecord> doService(String userId, ReqAccountTradeRecordList req) throws MerchantClientException {
		MerchantInfoEntity merchant = merchantInfoService.findByUserId(userId);
		if (merchant == null){
			throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}

		AccountRecordsQueryCondition condition = new AccountRecordsQueryCondition(merchant.getAccountId(), TbbConstants.RecordType.fromCode(req.getType()),null,null);
		TbbAccountResponse<TbbPage<AccountRecordInfo>> response = tbbAccountService.findAccountRecords(req.getPageSize(),req.getPageNo()-1, condition);

		PageDTO<ResAccountTradeRecord> page;
		List<ResAccountTradeRecord> voList = new ArrayList<>();
		if (response != null && response.isSucceeded() && response.getData() != null && response.getData().getList() != null && response.getData().getList().size() > 0){
			for(AccountRecordInfo recordInfo :response.getData().getList()){
				ResAccountTradeRecord vo = new ResAccountTradeRecord();
				BeanUtils.copyProperties(recordInfo,vo);
				voList.add(vo);
			}
			page = new PageDTO<>(response.getData().getPageNo(),response.getData().getPageSize(),response.getData().getTotal(),voList);
		}else {
			page = new PageDTO<>(req.getPageNo(),req.getPageSize(),0,voList);
		}
		return page;
	}
}
