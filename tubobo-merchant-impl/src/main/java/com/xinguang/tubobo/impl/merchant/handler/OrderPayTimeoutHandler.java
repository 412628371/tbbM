package com.xinguang.tubobo.impl.merchant.handler;

import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.impl.merchant.service.MerchantPushService;
import com.xinguang.tubobo.push.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/4/15.
 */
public class OrderPayTimeoutHandler  implements ChannelAwareMessageListener{
    @Autowired
    MerchantOrderService merchantOrderService;
    @Autowired
    MerchantPushService pushService;

    Logger logger = LoggerFactory.getLogger(OrderPayTimeoutHandler.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String orderNo = new String(message.getBody());
        MerchantOrderEntity entity = merchantOrderService.findByOrderNo(orderNo);
        if (null == entity){
            logger.error("订单超时未支付，未找到订单。orderNo:{}",orderNo);
            return;
        }
        merchantOrderService.payExpired(entity.getSenderId(),orderNo);
        logger.info("订单超时未支付，orderNo: {}",orderNo);
    }
}
