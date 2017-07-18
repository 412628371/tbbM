package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.impl.merchant.service.NoticeService;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumNoticeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xuqinghua on 2017/7/13.
 */
public class RmqNoticeHandler implements ChannelAwareMessageListener {
    Logger logger = LoggerFactory.getLogger(RmqNoticeHandler.class);
    @Autowired NoticeService noticeService;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String json = new String(message.getBody(),"utf-8");
        NoticeDTO dto = null;
        try {
            dto = JSON.parseObject(json,NoticeDTO.class);
        }catch (Exception e){
            logger.error("mq处理通知数据，数据转换失败。json:{}",json);
            return;
        }
        if (null == dto)
            return;

        if (EnumNoticeType.AUDIT.getValue().equals(dto.getNoticeType())){
            noticeService.pushAudit(dto);
        }
        if (EnumNoticeType.ORDER.getValue().equals(dto.getNoticeType())){
            noticeService.pushOrder(dto);
        }
    }
}
