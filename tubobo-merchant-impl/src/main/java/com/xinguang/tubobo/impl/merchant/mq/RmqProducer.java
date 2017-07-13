package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSONObject;
import com.xinguang.tubobo.merchant.api.dto.NoticeDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuqinghua on 2017/7/13.
 */
@Service
public class RmqProducer {
    @Autowired RabbitTemplate baseMqTemplate;

    public void sendNotice(NoticeDTO noticeDTO){
        baseMqTemplate.convertAndSend("tbb_notice_exchange","notice_bindingKey_merchant",
                JSONObject.toJSONString(noticeDTO));
    }
}
