package com.xinguang.tubobo.merchant.web.controller.fund;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.TbbConstants;
import com.xinguang.tubobo.account.api.response.AccountOperationInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;
import com.xinguang.tubobo.merchant.web.MerchantBaseController;
import com.xinguang.tubobo.merchant.web.request.ReqAccountTradeRecordDetail;
import com.xinguang.tubobo.merchant.web.response.trade.ResAccountTradeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by yanxu on 2017/8/24.
 */
@Controller
@RequestMapping(value = "/trade/record")
public class MerchantTradeDetailController extends MerchantBaseController<ReqAccountTradeRecordDetail,ResAccountTradeRecord> {

    @Autowired
    private TbbAccountService tbbAccountService;

    @Override
    protected ResAccountTradeRecord doService(String userId, ReqAccountTradeRecordDetail req) throws MerchantClientException {
        TbbAccountResponse<AccountOperationInfo> response = tbbAccountService.getAccountOperation(null, Long.valueOf(req.getRecordId()));
        ResAccountTradeRecord resAccountTradeRecord=null;
        if (response != null && response.isSucceeded() && response.getData() != null ){
             resAccountTradeRecord = convertToShow(response.getData());
        }else {
            throw new MerchantClientException(EnumRespCode.TRADE_RECORD_NOT_EXSIT);
        }
        return resAccountTradeRecord;
    }



    public ResAccountTradeRecord convertToShow(AccountOperationInfo operInfo){
        ResAccountTradeRecord record = new ResAccountTradeRecord();

        String amount = ConvertUtil.formatMoneyToString(operInfo.getAmount());
        record.setType(operInfo.getType().getLabel());
        record.setTradeStatus(operInfo.getRemarks());

        if(TbbConstants.OperationType.RECHARGE.equals(operInfo.getType())&&TbbConstants.OperationStatus.INIT.equals(operInfo.getStatus())){
            //商家取消充值 允许展示
            record.setType("未付款");
            record.setTradeStatus("充值取消");

            //return null;
        }
        if (TbbConstants.OperationType.FINE == operInfo.getType()){
            if (operInfo.getAmount() > 0){
                amount = "-"+amount;
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
                    record.setType("订单已完成");
                    record.setTradeStatus("订单支付");

                }
                //失败 退回
                if (TbbConstants.OperationStatus.CLOSE== operInfo.getStatus()){
                    amount = " 解冻"+amount;
                    record.setType("订单已取消");
                    record.setTradeStatus("订单支付");

                }
                //交易中
                if (TbbConstants.OperationStatus.INIT== operInfo.getStatus()){
                    amount = "冻结"+amount;
                    record.setType("订单进行中");
                    record.setTradeStatus("订单支付");

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
