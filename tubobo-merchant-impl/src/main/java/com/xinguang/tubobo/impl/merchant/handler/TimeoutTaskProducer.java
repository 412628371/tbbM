package com.xinguang.tubobo.impl.merchant.handler;

import com.xinguang.tubobo.impl.merchant.disconf.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/4/8.
 */
@Component
public class TimeoutTaskProducer {
    private Logger logger = LoggerFactory.getLogger(TimeoutTaskProducer.class);
    @Autowired
    @Qualifier(value = "delayMsgTemplate")
    private RabbitTemplate delayMsgTemplate;

    public void sendMessage(String msg,int expiredMillSeconds) {
        MessageProperties properties = new MessageProperties();
        properties.setExpiration(String.valueOf(expiredMillSeconds));
        Message message = new Message(msg.getBytes(),properties);
        logger.info("订单加入支付超时mq, orderNo:{},过期毫秒：{}",msg,expiredMillSeconds);
        delayMsgTemplate.convertAndSend("merchant_payExpired_delay_exchange","merchant_payExpired_routeKey_delay",message);
    }
}
