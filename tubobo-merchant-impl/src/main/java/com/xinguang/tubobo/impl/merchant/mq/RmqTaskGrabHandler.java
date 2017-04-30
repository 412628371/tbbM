package com.xinguang.tubobo.impl.merchant.mq;

import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantOrderService;
import com.xinguang.tubobo.merchant.api.dto.RiderGrabDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantOrderStatus;
import com.xinguang.tubobo.merchant.api.util.BeanBytesConvertionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xinguang.tubobo.merchant.api.util.BeanBytesConvertionUtil.ByteToObject;

/**
 * Created by Administrator on 2017/4/30.
 */
@Service
public class RmqTaskGrabHandler implements ChannelAwareMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RmqTaskGrabHandler.class);
    @Autowired
    private MerchantOrderService merchantOrderService;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        byte[] bytes = message.getBody();
        RiderGrabDTO riderGrabDTO = (RiderGrabDTO) BeanBytesConvertionUtil.ByteToObject(bytes);
        if (null == riderGrabDTO)
            return;
        logger.info("处理抢单回调。RiderGrabDTO：{}",riderGrabDTO.toString());
        MerchantOrderEntity order = merchantOrderService.findByOrderNo(riderGrabDTO.getTaskNo());
        if (order == null ||
                !EnumMerchantOrderStatus.WAITING_GRAB.getValue().equals(order.getOrderStatus())){
            merchantOrderService.riderGrabOrder(order.getUserId(),riderGrabDTO.getRiderId(),riderGrabDTO.getRiderName(),
                    riderGrabDTO.getRiderPhone(),riderGrabDTO.getTaskNo(),riderGrabDTO.getGrabTime());
        }
    }
}
