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
        try {
            String json = new String(message.getBody(),"utf-8");
            NoticeDTO dto = null;
            dto = JSON.parseObject(json,NoticeDTO.class);
            if (null == dto)
                return;

            if (EnumNoticeType.AUDIT.getValue().equals(dto.getNoticeType())){
                noticeService.pushAudit(dto);
            }
            if (EnumNoticeType.ORDER.getValue().equals(dto.getNoticeType())){
                noticeService.pushOrder(dto);
            }
            if (EnumNoticeType.SYSTEM.getValue().equals(dto.getNoticeType())){
                noticeService.pushSystem(dto);
            }
            if (EnumNoticeType.MONEY.getValue().equals(dto.getNoticeType())){
                noticeService.pushMoney(dto);
            }
            if (EnumNoticeType.BIND.getValue().equals(dto.getNoticeType())){
                noticeService.pushBind(dto);
            }
            if (EnumNoticeType.UNBIND.getValue().equals(dto.getNoticeType())){
                noticeService.pushUnbind(dto);
            }
            if (EnumNoticeType.MONEYSHORT.getValue().equals(dto.getNoticeType())){
                noticeService.pushMoneyShortForPostOrder(dto);
            }

        }catch (Exception e){
            logger.error("mq处理通知数据，异常：",e);
            return;
        }
    }
}
