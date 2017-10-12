package com.xinguang.tubobo.merchant.web.controller.fund;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.TbbConstants;
import com.xinguang.tubobo.account.api.TbbPage;
import com.xinguang.tubobo.account.api.request.AccountOperationsQueryCondition;
import com.xinguang.tubobo.account.api.request.AccountOperationsQueryConditionMultiType;
import com.xinguang.tubobo.account.api.request.AccountRecordsQueryCondition;
import com.xinguang.tubobo.account.api.response.AccountOperationInfo;
import com.xinguang.tubobo.account.api.response.AccountRecordInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqAccountTradeRecordList;
import com.xinguang.tubobo.merchant.web.response.trade.ResAccountTradeRecord;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/account/tradeRecordList")
//1.44版本使用
public class MerchantAccountTradeRecordListController extends MerchantBaseController<ReqAccountTradeRecordList,PageDTO<ResAccountTradeRecord>> {

	@Autowired
	private MerchantInfoService merchantInfoService;
	@Autowired
	private TbbAccountService tbbAccountService;

	@Override
	protected PageDTO<ResAccountTradeRecord> doService(String userId, ReqAccountTradeRecordList req) throws MerchantClientException {
		Date startTime = req.getStartTime();
		Date endTime = req.getEndTime();
		Date now=new Date();
		TbbConstants.OperationType type=TbbConstants.OperationType.fromCode(req.getType());
		//查询默认最近三个月的交易记录
		if (ReqAccountTradeRecordList.QueryTypeEnum.INIT.getValue().equals(req.getType())){
			endTime=new Date();
			startTime= DateUtils.addMonths(now,-3);
			startTime=DateUtils.getMinMonthDate(startTime);
		}


		MerchantInfoEntity merchant = merchantInfoService.findByUserId(userId);
		if (merchant == null){
			throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_EXISTS);
		}


		//v1.42后展示流水状态
		List<TbbConstants.OperationType> types=new ArrayList<>();
		if(null!=type){
			types.add(type);
		}
		if (TbbConstants.OperationType.RECHARGE.equals(type)){
			//充值类型展示=充值+充送
			types.add(TbbConstants.OperationType.GIFT);
		}
		AccountOperationsQueryConditionMultiType condition=new AccountOperationsQueryConditionMultiType(merchant.getAccountId(), types,null,startTime,endTime);
		TbbAccountResponse<TbbPage<AccountOperationInfo>> response = tbbAccountService.findAccountOperations(req.getPageSize(), req.getPageNo(), condition);


		PageDTO<ResAccountTradeRecord> page;
		List<ResAccountTradeRecord> voList = new ArrayList<>();
		if (response != null && response.isSucceeded() && response.getData() != null && response.getData().getList() != null && response.getData().getList().size() > 0){
			for(AccountOperationInfo recordInfo :response.getData().getList()){
				ResAccountTradeRecord resAccountTradeRecord = convertToShow(recordInfo);
				if (resAccountTradeRecord!=null){
					voList.add(resAccountTradeRecord);
				}
			}
			page = new PageDTO<>(response.getData().getPageNo(),response.getData().getPageSize(),response.getData().getTotal(),voList);
		}else {
			page = new PageDTO<>(req.getPageNo(),req.getPageSize(),0,voList);
		}
		return page;
	}



	public static ResAccountTradeRecord convertToShow(AccountOperationInfo operInfo){
		ResAccountTradeRecord record = new ResAccountTradeRecord();

		String amount = ConvertUtil.formatMoneyToString(operInfo.getAmount());
		record.setType(operInfo.getType().getLabel());
		record.setTradeStatus(operInfo.getRemarks());

		if(TbbConstants.OperationType.RECHARGE.equals(operInfo.getType())&&TbbConstants.OperationStatus.INIT.equals(operInfo.getStatus())){
			//商家取消充值 允许展示
			record.setTradeStatus("充值取消");

			//return null;
		}
		if (TbbConstants.OperationType.FINE == operInfo.getType()){
			if (operInfo.getAmount() > 0){
				amount = "-"+amount;
			}
			if (MerchantConstants.MERCHANT_MESSAGE_REMARK.equalsIgnoreCase(operInfo.getRemarks())){
				record.setType("短信费");
				record.setTradeStatus("已发送");

			}
		}
		if(TbbConstants.OperationType.RECHARGE.equals(operInfo.getType())&&TbbConstants.OperationStatus.SUCCEED.equals(operInfo.getStatus())){
			//商家充值,
			record.setType(null);
			amount = amount;
			record.setType("账户充值");
			record.setTradeStatus("账户充值");
		}
		if (TbbConstants.OperationType.PAY == operInfo.getType()){
			if (operInfo.getAmount() > 0){
				// 成功
				if (TbbConstants.OperationStatus.SUCCEED== operInfo.getStatus()){
					amount = "-"+amount;
					record.setType("订单支付");
					record.setTradeStatus("订单已完成");

				}
				//失败 退回
				if (TbbConstants.OperationStatus.CLOSE== operInfo.getStatus()){
					amount = " 解冻"+amount;
					record.setType("订单支付");
					record.setTradeStatus("订单已取消");

				}
				//交易中
				if (TbbConstants.OperationStatus.INIT== operInfo.getStatus()){
					amount = "冻结"+amount;
					record.setType("订单支付");
					record.setTradeStatus("订单进行中");

				}

			}
		}
		if (TbbConstants.OperationType.WITHDRAW == operInfo.getType()){
			if (operInfo.getAmount() > 0){
				amount = "-"+amount;
			}
		}
		record.setAmount(amount);
		record.setCreateTime(operInfo.getCreateTime());
		record.setRecordId(operInfo.getId().toString());
		record.setOrderNo(operInfo.getOrderId());


		return record;
	}

	@Override
	protected boolean needIdentify() {
		return true;
	}
}