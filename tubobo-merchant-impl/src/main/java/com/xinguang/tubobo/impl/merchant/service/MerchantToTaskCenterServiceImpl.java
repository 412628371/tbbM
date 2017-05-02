package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.account.api.request.PayConfirmRequest;
import com.xinguang.tubobo.account.api.response.PayInfo;
import com.xinguang.tubobo.account.api.response.TbbAccountResponse;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.push.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class MerchantToTaskCenterServiceImpl implements MerchantToTaskCenterServiceInterface {

    Logger logger = LoggerFactory.getLogger(MerchantToTaskCenterServiceImpl.class);
    @Autowired
    MerchantPushService pushService;
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
        MerchantOrderEntity entity = merchantOrderService.findByOrderNoAndStatus(orderNo,
                EnumMerchantOrderStatus.WAITING_GRAB.getValue());
        if (null == entity){
            logger.error("骑手已接单通知，未找到订单或已处理接单。orderNo:{}",orderNo);
            return false;
        }
        logger.info("处理骑手接单：orderNo:{}",orderNo);
        boolean result = merchantOrderService.riderGrabOrder(entity.getUserId(),riderId,riderName,riderPhone,orderNo,grabOrderTime) > 0;
        if (result){
            pushService.noticeGrab(entity.getUserId());
        }
        return result;

    }

    /**
     * 骑手取货
     * @param orderNo
     * @param grabItemTime
     */
    @Override
    public boolean riderGrabItem(String orderNo, Date grabItemTime) {
        MerchantOrderEntity entity = merchantOrderService.findByOrderNoAndStatus(orderNo,
                EnumMerchantOrderStatus.WAITING_PICK.getValue());
        if (null == entity){
            logger.info("骑手取货，未找到订单或已取货完成。orderNo:{}",orderNo);
            return false;
        }
        logger.info("处理骑手取货：orderNo:{}",orderNo);
        return merchantOrderService.riderGrabItem(entity.getUserId(), orderNo,grabItemTime) > 0;
    }

    /**
     * 骑手完成任务
     * @param orderNo
     * @param finishOrderTime
     */
    @Override
    public boolean riderFinishOrder(String orderNo, Date finishOrderTime) {
        MerchantOrderEntity entity = merchantOrderService.findByOrderNo(orderNo);
        if (null == entity || EnumMerchantOrderStatus.FINISH.getValue().equals(entity.getOrderStatus())){
            logger.info("骑手完成配送，未找到订单或订单已完成。orderNo:{}",orderNo);
            return false;
        }
        logger.info("处理骑手送达完成：orderNo:{}",orderNo);
        boolean result =  merchantOrderService.riderFinishOrder(entity.getUserId(),orderNo,finishOrderTime) > 0;
        if (result){
            //发送骑手完成送货通知
            pushService.noticeFinished(entity.getUserId());
        }
        return result;
    }

    @Override
    public boolean orderExpire(String orderNo,Date expireTime) {
       return merchantOrderService.dealGrabOvertimeOrders(orderNo,expireTime,true);
    }

//    public boolean dealGrabOvertimeOrders(String orderNo,Date expireTime,boolean enablePushNotice){
//        MerchantOrderEntity entity = merchantOrderService.findByOrderNoAndStatus(orderNo, EnumMerchantOrderStatus.WAITING_GRAB.getValue());
//        if (null == entity ){
//            logger.info("超时无人接单。订单不存在或状态不允许超时取消，orderNo: "+orderNo);
//            return false;
//        }
//        logger.info("处理超时无人接单：orderNo:{}",orderNo);
//        PayConfirmRequest confirmRequest = PayConfirmRequest.getInstanceOfReject(entity.getPayId(),
//                MerchantConstants.PAY_REJECT_REMARKS_OVERTIME);
//        TbbAccountResponse<PayInfo> resp =  tbbAccountService.payConfirm(confirmRequest);
//        if (resp != null && resp.isSucceeded()){
//            logger.info("超时无人接单，资金平台退款成功，userId: "+entity.getUserId()+" orderNo: "+orderNo+
//                    "errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
//            merchantOrderService.orderExpire(entity.getSenderId(),orderNo,expireTime);
//            if (enablePushNotice){
//                pushService.noticeGrabTimeout(entity.getUserId());
//            }
//            return true;
//        }else {
//            if (resp == null){
//                logger.error("超时无人接单，资金平台退款出错，userId: "+entity.getUserId()+" orderNo: "+orderNo);
//            }else {
//                logger.error("超时无人接单，资金平台退款出错，userId: "+entity.getUserId()+" orderNo: "+orderNo+
//                        "errorCode: "+ resp.getErrorCode()+"message: "+resp.getMessage());
//            }
//        }
//        return false;
//    }

}
