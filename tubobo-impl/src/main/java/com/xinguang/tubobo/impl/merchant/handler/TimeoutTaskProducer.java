package com.xinguang.tubobo.impl.merchant.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/8.
 */
@Component
public class TimeoutTaskProducer {
    private Logger logger = LoggerFactory.getLogger(TimeoutTaskProducer.class);
    @Autowired
    @Qualifier(value = "delayMsgTemplate")
    private RabbitTemplate delayMsgTemplate;

    public void sendMessage(Object message, final int timeoutOffset) {
        logger.info("to send message:{}",message);
//        final int xdelay= 5*1000;
        final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        logger.info("消息加入延时队列："+message.toString());
        //发送延迟消息
        delayMsgTemplate.convertAndSend("task.delay.notify", message,
                new MessagePostProcessor() {
                    @Override
                    public Message postProcessMessage(Message message)
                            throws AmqpException {
                        //设置消息持久化
                        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        //设置延迟时间（5分钟后执行）
                        message.getMessageProperties().setDelay(timeoutOffset);
                        logger.info("----"+sf.format(new Date()) + " Delay sent.");
                        return message;
                    }
                });
    }
}
