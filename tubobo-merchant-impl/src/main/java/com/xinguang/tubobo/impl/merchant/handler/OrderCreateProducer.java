package com.xinguang.tubobo.impl.merchant.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by XG on 2017/6/20.
 */
@Component
public class OrderCreateProducer {

    private Logger logger = LoggerFactory.getLogger(OrderCreateProducer.class);

    @Autowired
    @Qualifier(value = "accessMsgTemplate")
    private RabbitTemplate accessMsgTemplate;

    public void sendMessage(String msg){
        Message message = new Message(msg.getBytes(), null);
        logger.info("订单创建完加入mq, orderNo:{}",msg);
        accessMsgTemplate.convertAndSend("merchant_orderFinished_create_exchange","merchant_orderFinished_routeKey_create",message);
    }
}
