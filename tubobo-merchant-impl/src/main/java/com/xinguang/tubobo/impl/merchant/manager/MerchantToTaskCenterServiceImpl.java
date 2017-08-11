package com.xinguang.tubobo.impl.merchant.manager;

import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class MerchantToTaskCenterServiceImpl implements MerchantToTaskCenterServiceInterface {

   private static final Logger logger = LoggerFactory.getLogger(MerchantToTaskCenterServiceImpl.class);
    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Autowired
    private OrderService orderService;


    /**
     * 骑手抢单
     * @param dto
     * @return
     */
    @Override
    public boolean riderGrabOrder(MerchantGrabCallbackDTO dto) {

        return merchantOrderManager.riderGrabOrder(dto,true);

    }

    /**
     * 骑手取货
     * @param orderNo
     * @param grabItemTime
     */
    @Override
    public boolean riderGrabItem(String orderNo, Date grabItemTime) {

        return merchantOrderManager.riderGrabItem(orderNo,grabItemTime,true);
    }

    /**
     * 骑手完成任务
     * @param orderNo
     * @param finishOrderTime
     */
    @Override
    public boolean riderFinishOrder(String orderNo, Date finishOrderTime) {

        return   merchantOrderManager.riderFinishOrder(orderNo,finishOrderTime,true);

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
