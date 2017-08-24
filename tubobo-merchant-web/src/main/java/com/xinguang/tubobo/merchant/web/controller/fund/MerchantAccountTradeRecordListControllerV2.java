package com.xinguang.tubobo.merchant.web.controller.fund;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.TbbConstants;
import com.xinguang.tubobo.account.api.TbbPage;
import com.xinguang.tubobo.account.api.request.AccountOperationsQueryCondition;
import com.xinguang.tubobo.account.api.response.AccountOperationInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqAccountTradeRecordList;
import com.xinguang.tubobo.merchant.web.response.ResAccountTradeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanxu on 2017/8/24.
 */
@Controller
@RequestMapping(value = "/account/tradeRecordListV2")
public class MerchantAccountTradeRecordListControllerV2 extends MerchantBaseController<ReqAccountTradeRecordList,PageDTO<ResAccountTradeRecord>> {

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


        //v1.42后展示流水状态
        AccountOperationsQueryCondition condition=new AccountOperationsQueryCondition(merchant.getAccountId(), TbbConstants.OperationType.fromCode(req.getType()),null,null,null);
        TbbAccountResponse<TbbPage<AccountOperationInfo>> response = tbbAccountService.findAccountOperations(req.getPageSize(), req.getPageNo(), condition);


        PageDTO<ResAccountTradeRecord> page;
        List<ResAccountTradeRecord> voList = new ArrayList<>();
        if (response != null && response.isSucceeded() && response.getData() != null && response.getData().getList() != null && response.getData().getList().size() > 0){
            for(AccountOperationInfo recordInfo :response.getData().getList()){

                voList.add(convertToShow(recordInfo));
            }
            page = new PageDTO<>(response.getData().getPageNo(),response.getData().getPageSize(),response.getData().getTotal(),voList);
        }else {
            page = new PageDTO<>(req.getPageNo(),req.getPageSize(),0,voList);
        }
        return page;
    }



    public ResAccountTradeRecord convertToShow(AccountOperationInfo operInfo){
        ResAccountTradeRecord record = new ResAccountTradeRecord();
        String amount = ConvertUtil.formatMoneyToString(operInfo.getAmount());
        record.setType(operInfo.getType().getLabel());
        if (TbbConstants.OperationType.FINE == operInfo.getType()){
            if (operInfo.getAmount() > 0){
                amount = "-"+amount;
            }
        }
        if (TbbConstants.OperationType.PAY == operInfo.getType()){
            if (operInfo.getAmount() > 0){
                // 成功
                if (TbbConstants.OperationStatus.SUCCEED== operInfo.getStatus()){
                    amount = "-"+amount+"元";
                    record.setType("订单支付");
                }
                //失败 退回
                if (TbbConstants.OperationStatus.CLOSE== operInfo.getStatus()){
                    amount = " 解冻金额"+amount+"元";
                    record.setType("订单取消");
                }
                //交易中
                if (TbbConstants.OperationStatus.INIT== operInfo.getStatus()){
                    amount = "冻结金额"+amount+"元";
                    record.setType("订单支付");
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
