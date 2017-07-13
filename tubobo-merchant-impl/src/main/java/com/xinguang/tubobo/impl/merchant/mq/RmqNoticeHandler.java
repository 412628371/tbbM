package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

/**
 * Created by xuqinghua on 2017/7/13.
 */
public class RmqNoticeHandler implements ChannelAwareMessageListener {
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String json = new String(message.getBody(),"utf-8");
        NoticeDTO dto = JSON.parseObject(json,NoticeDTO.class);
        if (dto.getNoticeType() == 1){

        }
    }
}
