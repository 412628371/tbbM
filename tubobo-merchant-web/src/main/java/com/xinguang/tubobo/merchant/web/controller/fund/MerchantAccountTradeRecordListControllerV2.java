package com.xinguang.tubobo.merchant.web.controller.fund;

import com.hzmux.hzcms.common.utils.DateUtils;
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
import com.xinguang.tubobo.merchant.web.response.trade.ResAccountTradeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
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
        AccountOperationsQueryCondition condition=new AccountOperationsQueryCondition(merchant.getAccountId(), type,null,startTime,endTime);
        TbbAccountResponse<TbbPage<AccountOperationInfo>> response = tbbAccountService.findAccountOperations(req.getPageSize(), req.getPageNo(), condition);


        PageDTO<ResAccountTradeRecord> page;
        List<ResAccountTradeRecord> voList = new ArrayList<>();
        if (response != null && response.isSucceeded() && response.getData() != null && response.getData().getList() != null && response.getData().getList().size() > 0){
            for(AccountOperationInfo recordInfo :response.getData().getList()){
                ResAccountTradeRecord resAccountTradeRecord = MerchantAccountTradeRecordListController.convertToShow(recordInfo);
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





    @Override
    protected boolean needIdentify() {
        return true;
    }
}
