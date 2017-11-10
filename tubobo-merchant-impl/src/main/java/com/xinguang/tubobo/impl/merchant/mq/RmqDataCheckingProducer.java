package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSON;
import com.xinguang.tubobo.checking.DataCheckingDTO;
import com.xinguang.tubobo.checking.TbbCheckingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;


/**
 * 将RPC调用异常信息发送到MQ
 * @author xuqinghua
 */
@Service
public class RmqDataCheckingProducer {

    private Logger logger = LoggerFactory.getLogger(RmqDataCheckingProducer.class);

    @Autowired
    @Qualifier("baseMqTemplate")
    private RabbitTemplate baseMqTemplate;

    public void sendMessage(DataCheckingDTO dto){
        try {
            String msg = JSON.toJSONString(dto);
            baseMqTemplate.convertAndSend(TbbCheckingConstants.DATA_CHECKING_QUEUE,msg);
            logger.info("报告处理异常消息, {}",msg);
        } catch (Exception e) {
            logger.error("报告处理异常消息,发送到MQ异常:{}",dto.toString());
        }
    }
}
