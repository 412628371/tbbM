package com.xinguang.tubobo.impl.merchant.manager;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantInfoService;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantInfoDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantUnsettledDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MerchantToTaskCenterServiceImpl implements MerchantToTaskCenterServiceInterface {

   private static final Logger logger = LoggerFactory.getLogger(MerchantToTaskCenterServiceImpl.class);
    @Autowired
    private MerchantOrderManager merchantOrderManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MerchantInfoService merchantInfoService;

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
     * 骑手完成任务(废弃 由消息通知)
     * @param orderNo
     * @param finishOrderTime
     */
    @Override
    public boolean riderFinishOrder(String orderNo, Date finishOrderTime) {

         return   merchantOrderManager.riderFinishOrder(orderNo,finishOrderTime,null,null,true);

    }

    @Override
    public boolean orderExpire(String orderNo,Date expireTime) {
       return merchantOrderManager.dealGrabOvertimeOrders(orderNo,expireTime,true);
    }

    @Override
    public boolean adminCancel(String orderNo, Date cancelTime)  {
        boolean flag=true;
        MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
        if (null == entity ){
            logger.warn("后台取消订单，订单不存在。orderNo:{}",orderNo);
            flag= false;
        }
        try {
            flag=merchantOrderManager.cancelOrder(entity.getUserId(),orderNo,true,null);
        } catch (MerchantClientException e) {
            logger.error(e.getMessage());
            flag= false;
        }
        return flag;
    }

    @Override
    public boolean riderUnsettledOrder(MerchantUnsettledDTO dto) {
        return merchantOrderManager.riderUnsettledOrder(dto);
    }

    @Override
    public List<MerchantInfoDTO> findAllByProviderId(long providerId) {
        List<MerchantInfoDTO> list = new ArrayList<>();
        List<MerchantInfoEntity> merchantInfoEntities = merchantInfoService.findAllByProviderId(providerId);
        if(null != merchantInfoEntities && merchantInfoEntities.size()>0){
            for(MerchantInfoEntity entity : merchantInfoEntities){
                MerchantInfoDTO dto = new MerchantInfoDTO();
                BeanUtils.copyProperties(entity, dto);
                list.add(dto);
            }
        }
        return list;
    }
}
