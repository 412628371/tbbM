package com.xinguang.tubobo.impl.merchant.handler;

import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.impl.merchant.cache.RedisOp;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.OrderService;
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
    OrderService orderService;

    @Autowired
    RedisOp redisOp;
    Logger logger = LoggerFactory.getLogger(OrderPayTimeoutHandler.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String orderNo = new String(message.getBody());
            MerchantOrderEntity entity = orderService.findByOrderNo(orderNo);
            if (null == entity){
                logger.error("订单超时未支付，未找到订单。orderNo:{}",orderNo);
                return;
            }
            orderService.payExpired(entity.getUserId(),orderNo);
            redisOp.evictCache("merchantOrder_"+entity.getUserId()+"_*");
            logger.info("订单超时未支付，orderNo: {}",orderNo);
        }catch (Exception e){
            logger.error("mq处理异常",e);
        }

    }
}
