package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.dubbo.common.json.JSON;
import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.impl.merchant.handler.ThirdCallbackHandler;
import com.xinguang.tubobo.takeout.mt.MtNotifyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xuqinghua on 2017/6/30.
 */
public class RmqTakeoutCallbackHandler implements ChannelAwareMessageListener {
    Logger logger = LoggerFactory.getLogger(RmqTakeoutCallbackHandler.class);
    @Autowired private ThirdCallbackHandler thirdCallbackHandler;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String body = new String(message.getBody(),"utf-8");
        logger.info("收到外卖平台信息：{}",body);
        MtNotifyDTO mtNotifyDTO = JSON.parse(body,MtNotifyDTO.class);
        switch (mtNotifyDTO.getPlatformCode()){
            case MT:
                thirdCallbackHandler.dispatch(mtNotifyDTO.getPlatformCode(),mtNotifyDTO.getNotifyType(),mtNotifyDTO.getMerchantId(),mtNotifyDTO.getJsonData());
                break;
            case ELE:
                thirdCallbackHandler.dispatch(mtNotifyDTO.getPlatformCode(),mtNotifyDTO.getNotifyType(),mtNotifyDTO.getMerchantId(),mtNotifyDTO.getJsonData());
                break;
            case YZ:
                thirdCallbackHandler.dispatch(mtNotifyDTO.getPlatformCode(),mtNotifyDTO.getNotifyType(),mtNotifyDTO.getMerchantId(),mtNotifyDTO.getJsonData());
                break;
            case BD:
                thirdCallbackHandler.dispatch(mtNotifyDTO.getPlatformCode(),mtNotifyDTO.getNotifyType(),mtNotifyDTO.getMerchantId(),mtNotifyDTO.getJsonData());
                break;
            default:

        }
    }
}
