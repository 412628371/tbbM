package com.xinguang.tubobo.impl.merchant.mq;

import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantTaskOperatorCallbackDTO;
import com.xinguang.tubobo.merchant.api.util.BeanBytesConvertionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/30.
 */
@Service
public class RmqTaskOperateHandler implements ChannelAwareMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RmqTaskOperateHandler.class);
    @Autowired
    MerchantToTaskCenterServiceInterface merchantToTaskCenterServiceInterface;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        byte[] bytes = message.getBody();
        MerchantTaskOperatorCallbackDTO merchantTaskOperatorCallbackDTO = (MerchantTaskOperatorCallbackDTO) BeanBytesConvertionUtil.ByteToObject(bytes);
        if (null == merchantTaskOperatorCallbackDTO)
            return;
        logger.info("处理抢单回调。MerchantGrabCallbackDTO：{}", merchantTaskOperatorCallbackDTO.toString());
        if (MerchantTaskOperatorCallbackDTO.EnumTaskOperatorType.PICK.getValue().
                equals(merchantTaskOperatorCallbackDTO.getTaskOperatorType().getValue())){
            merchantToTaskCenterServiceInterface.riderGrabItem(merchantTaskOperatorCallbackDTO.getTaskNo(), merchantTaskOperatorCallbackDTO.getOperateTime());
        }
        if (MerchantTaskOperatorCallbackDTO.EnumTaskOperatorType.FINISH.getValue().
                equals(merchantTaskOperatorCallbackDTO.getTaskOperatorType().getValue())){
            merchantToTaskCenterServiceInterface.riderFinishOrder(merchantTaskOperatorCallbackDTO.getTaskNo(), merchantTaskOperatorCallbackDTO.getOperateTime());
        }
        if (MerchantTaskOperatorCallbackDTO.EnumTaskOperatorType.EXPIRED.getValue().
                equals(merchantTaskOperatorCallbackDTO.getTaskOperatorType().getValue())){
            merchantToTaskCenterServiceInterface.orderExpire(merchantTaskOperatorCallbackDTO.getTaskNo(), merchantTaskOperatorCallbackDTO.getOperateTime());
        }
    }
}
