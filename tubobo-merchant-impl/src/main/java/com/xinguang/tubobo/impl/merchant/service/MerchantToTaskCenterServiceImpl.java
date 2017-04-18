package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class MerchantToTaskCenterServiceImpl implements MerchantToTaskCenterServiceInterface {

    Logger logger = LoggerFactory.getLogger(MerchantToTaskCenterServiceImpl.class);
    @Autowired
    private MerchantOrderService merchantOrderService;

    @Autowired
    private TbbAccountService tbbAccountService;
    /**
     * 骑手抢单
     * @param riderId
     * @param riderName
     * @param riderPhone
     * @param orderNo
     * @param grabOrderTime
     */
    @Override
    public boolean riderGrabOrder(String riderId, String riderName, String riderPhone, String orderNo, Date grabOrderTime) {
        //TODO 发送被抢单通知
        return merchantOrderService.riderGrabOrder(riderId,riderName,riderPhone,orderNo,grabOrderTime) > 0;
    }

    /**
     * 骑手取货
     * @param orderNo
     * @param grabItemTime
     */
    @Override
    public boolean riderGrabItem(String orderNo, Date grabItemTime) {
        //TODO 发送骑手取货通知
        return merchantOrderService.riderGrabItem(orderNo,grabItemTime) > 0;
    }

    /**
     * 骑手完成任务
     * @param orderNo
     * @param finishOrderTime
     */
    @Override
    public boolean riderFinishOrder(String orderNo, Date finishOrderTime) {
        //TODO 发送骑手完成送货通知
        return merchantOrderService.riderFinishOrder(orderNo,finishOrderTime) > 0;
    }

    @Override
    public boolean orderExpire(String orderNo) {
        MerchantOrderEntity entity = merchantOrderService.findByOrderNo(orderNo);
        if (null == entity)
            return false;
        logger.error("超时无人接单,退款出错。订单不存在，orderNo: "+orderNo);
        PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(entity.getPayId(),
                MerchantConstants.PAY_REJECT_REMARKS_OVERTIME);
        TbbAccountResponse<PayInfo> resp =  tbbAccountService.payConfirm(confirmRequest);
        if (resp != null || resp.isSucceeded()){
            logger.error("超时无人接单，资金平台退款成功，userId: "+entity.getUserId()+" orderNo: "+orderNo+
                    "errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
            merchantOrderService.orderExpire(orderNo);
            return true;
        }else {
            logger.error("超时无人接单，资金平台退款出错，userId: "+entity.getUserId()+" orderNo: "+orderNo+
                    "errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
        }
        return false;
    }

}
