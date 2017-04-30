package com.xinguang.tubobo.impl.merchant.mq;

import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.TaskOperatorDTO;
import com.xinguang.tubobo.merchant.api.util.BeanBytesConvertionUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/30.
 */
@Service
public class RmqTaskOperateHandler implements ChannelAwareMessageListener {
    @Autowired
    MerchantToTaskCenterServiceInterface merchantToTaskCenterServiceInterface;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        byte[] bytes = message.getBody();
        TaskOperatorDTO taskOperatorDTO = (TaskOperatorDTO) BeanBytesConvertionUtil.ByteToObject(bytes);
        if (null == taskOperatorDTO)
            return;
        if (TaskOperatorDTO.EnumTaskOperatorType.PICK.getValue().
                equals(taskOperatorDTO.getTaskOperatorType().getValue())){
            merchantToTaskCenterServiceInterface.riderGrabItem(taskOperatorDTO.getTaskNo(),taskOperatorDTO.getOperateTime());
        }
        if (TaskOperatorDTO.EnumTaskOperatorType.FINISH.getValue().
                equals(taskOperatorDTO.getTaskOperatorType().getValue())){
            merchantToTaskCenterServiceInterface.riderFinishOrder(taskOperatorDTO.getTaskNo(),taskOperatorDTO.getOperateTime());
        }
        if (TaskOperatorDTO.EnumTaskOperatorType.EXPIRED.getValue().
                equals(taskOperatorDTO.getTaskOperatorType().getValue())){
            merchantToTaskCenterServiceInterface.orderExpire(taskOperatorDTO.getTaskNo(),taskOperatorDTO.getOperateTime());
        }
    }
}
