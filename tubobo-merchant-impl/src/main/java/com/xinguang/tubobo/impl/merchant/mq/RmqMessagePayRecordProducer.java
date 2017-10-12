package com.xinguang.tubobo.impl.merchant.mq;

import com.xinguang.tubobo.merchant.api.MerchantClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


/**
 * 存储地址信息
 */
@Service
public class RmqMessagePayRecordProducer {

    private Logger logger = LoggerFactory.getLogger(RmqMessagePayRecordProducer.class);

    @Autowired
    private RabbitTemplate baseMqTemplate;
    public void sendMessage(String msg){
        MessageProperties properties = new MessageProperties();
        Message message = null;
        try {
            message = new Message(msg.getBytes("utf-8"), properties);
        } catch (UnsupportedEncodingException e) {
            logger.error("发送消息异常:"+message);
        }

        logger.info("创建保存短信交易记录消息, orderNo:{}",msg);
        baseMqTemplate.convertAndSend("merchant_biz_exchange","merchant_messageRecord_queue",message);
    }
}
