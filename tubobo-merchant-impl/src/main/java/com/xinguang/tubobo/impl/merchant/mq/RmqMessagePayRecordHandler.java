package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageRecordEntity;
import com.xinguang.tubobo.impl.merchant.handler.ThirdCallbackHandler;
import com.xinguang.tubobo.impl.merchant.service.MessageRecordService;
import com.xinguang.tubobo.takeout.mt.MtNotifyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xuqinghua on 2017/6/30.
 */
public class RmqMessagePayRecordHandler implements ChannelAwareMessageListener {
    Logger logger = LoggerFactory.getLogger(RmqMessagePayRecordHandler.class);
    @Autowired private ThirdCallbackHandler thirdCallbackHandler;
    @Autowired private MessageRecordService messageRecordService;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        try {
            String body = new String(message.getBody(),"utf-8");
            logger.info("收到短信交易记录消息：{}",body);
            MerchantMessageRecordEntity entity = JSON.toJavaObject(JSONObject.parseObject(body),MerchantMessageRecordEntity.class);
            messageRecordService.saveEntity(entity);
        }catch (Exception e){
            logger.error("保存短信交易记录消息，出错",e);
        }
    }
}
