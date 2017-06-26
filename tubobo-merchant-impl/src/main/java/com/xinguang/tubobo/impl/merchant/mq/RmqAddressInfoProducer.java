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

/**
 * 存储地址信息
 */
@Service
public class RmqAddressInfoProducer {

    private Logger logger = LoggerFactory.getLogger(RmqAddressInfoProducer.class);

    @Autowired
    @Qualifier(value = "addressInfoTemplate")
    private RabbitTemplate addressInfoTemplate;

    public void sendMessage(String msg) throws MerchantClientException {
        MessageProperties properties = new MessageProperties();
        Message message = new Message(msg.getBytes(), properties);
        logger.info("订单创建完加入mq存储地址信息, orderNo:{}",msg);
        addressInfoTemplate.convertAndSend("merchant_addressInfo_query_exchange","merchant_addressInfo_routeKey_normal",message);
    }
}
