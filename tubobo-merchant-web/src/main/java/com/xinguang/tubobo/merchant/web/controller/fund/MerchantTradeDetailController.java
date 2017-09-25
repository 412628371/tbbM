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
             resAccountTradeRecord = MerchantAccountTradeRecordListController.convertToShow(response.getData());
        }else {
            throw new MerchantClientException(EnumRespCode.TRADE_RECORD_NOT_EXSIT);
        }
        return resAccountTradeRecord;
    }




    @Override
    protected boolean needIdentify() {
        return true;
    }
}
