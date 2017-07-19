package com.xinguang.tubobo.impl.merchant.manager;

import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.account.api.TbbAccountService;
import com.xinguang.tubobo.impl.merchant.mq.RmqNoticeProducer;
import com.xinguang.tubobo.impl.merchant.mq.TuboboReportDateMqHelp;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class MerchantToTaskCenterServiceImpl implements MerchantToTaskCenterServiceInterface {

    Logger logger = LoggerFactory.getLogger(MerchantToTaskCenterServiceImpl.class);
    @Autowired
    RmqNoticeProducer rmqNoticeProducer;
    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TbbAccountService tbbAccountService;
    @Autowired
    private TuboboReportDateMqHelp tuboboReportDateMqHelp;

    /**
     * 骑手抢单
     * @param dto
     * @return
     */
    @Override
    public boolean riderGrabOrder(MerchantGrabCallbackDTO dto) {
        if (null == dto || StringUtils.isBlank(dto.getTaskNo())){
            logger.error("抢单回调dto为空");
        }
        logger.info("抢单回调：dto:{}",dto.toString());
        String orderNo = dto.getTaskNo();
        MerchantOrderEntity entity = orderService.findByOrderNoAndStatus(orderNo,
                EnumMerchantOrderStatus.WAITING_GRAB.getValue());
        if (null == entity){
            logger.error("骑手已接单通知，未找到订单或已处理接单。orderNo:{}",orderNo);
            return false;
        }
        logger.info("处理骑手接单：orderNo:{}",orderNo);
        boolean result = orderService.riderGrabOrder(entity.getUserId(),dto.getRiderId(),dto.getRiderName(),dto.getRiderPhone(),
                orderNo,dto.getGrabTime(),dto.getExpectFinishTime(),dto.getRiderCarNo(),dto.getRiderCarType()) > 0;
        if (result){
            rmqNoticeProducer.sendGrabNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());
//            pushService.noticeGrab(entity.getUserId(),orderNo, MerchantConstants.getPushParamByOrderType(entity.getOrderType()));
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
        MerchantOrderEntity entity = orderService.findByOrderNoAndStatus(orderNo,
                EnumMerchantOrderStatus.WAITING_PICK.getValue());
        if (null == entity){
            logger.info("骑手取货，未找到订单或已取货完成。orderNo:{}",orderNo);
            return false;
        }
        logger.info("处理骑手取货：orderNo:{}",orderNo);
        return merchantOrderManager.riderGrabItem(entity.getUserId(), orderNo,grabItemTime) > 0;
    }

    /**
     * 骑手完成任务
     * @param orderNo
     * @param finishOrderTime
     */
    @Override
    public boolean riderFinishOrder(String orderNo, Date finishOrderTime) {
        MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
        if (null == entity || EnumMerchantOrderStatus.FINISH.getValue().equals(entity.getOrderStatus())){
            logger.info("骑手完成配送，未找到订单或订单已完成。orderNo:{}",orderNo);
            return false;
        }
        logger.info("处理骑手送达完成：orderNo:{}",orderNo);
        boolean result =  merchantOrderManager.riderFinishOrder(entity.getUserId(),orderNo,finishOrderTime) > 0;
        if (result){
            //发送骑手完成送货通知
//            pushService.noticeFinished(entity.getUserId(),orderNo,MerchantConstants.getPushParamByOrderType(entity.getOrderType()));
            rmqNoticeProducer.sendOrderFinishNotice(entity.getUserId(),orderNo,entity.getOrderType(),entity.getPlatformCode(),entity.getOriginOrderViewId());

            //推送到报表mq
            tuboboReportDateMqHelp.orderFinish(entity,finishOrderTime);
        }
        return result;
    }

    @Override
    public boolean orderExpire(String orderNo,Date expireTime) {
       return merchantOrderManager.dealGrabOvertimeOrders(orderNo,expireTime,true);
    }

    @Override
    public boolean adminCancel(String orderNo, Date cancelTime) {
        MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
        if (null == entity ){
            logger.warn("后台取消订单，订单不存在。orderNo:{}",orderNo);
            return false;
        }
        return merchantOrderManager.cancelOrder(entity.getUserId(),orderNo,true);
    }


}
