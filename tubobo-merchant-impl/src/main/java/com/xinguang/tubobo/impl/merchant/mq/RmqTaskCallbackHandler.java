package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.xinguang.tubobo.merchant.api.MerchantToTaskCenterServiceInterface;
import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantTaskOperatorCallbackDTO;
import com.xinguang.tubobo.merchant.api.dto.TaskCallbackDTO;
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
public class RmqTaskCallbackHandler implements ChannelAwareMessageListener {
    private static final Logger logger = LoggerFactory.getLogger(RmqTaskCallbackHandler.class);
    @Autowired
    MerchantToTaskCenterServiceInterface merchantToTaskCenterServiceInterface;
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {

        String plain = new String(message.getBody(),"utf-8");
        TaskCallbackDTO taskCallbackDTO = JSON.parseObject(plain, TaskCallbackDTO.class);
        if (null == taskCallbackDTO) {
            return;
        }
        //TODO 处理各种操作
        String json = taskCallbackDTO.getJsonContent();
//        JSONObject jo = JSONObject.parseObject(json);
        switch (taskCallbackDTO.getType()){
            case GRAB:
                MerchantGrabCallbackDTO dtoGrab =
                        JSON.parseObject(json,MerchantGrabCallbackDTO.class);
                merchantToTaskCenterServiceInterface.riderGrabOrder(dtoGrab);
                break;
            case PICK:
                MerchantTaskOperatorCallbackDTO dtoPick =
                        JSON.parseObject(json,MerchantTaskOperatorCallbackDTO.class);
                merchantToTaskCenterServiceInterface.riderGrabItem(dtoPick.getTaskNo(),dtoPick.getOperateTime());
                break;
            case FINISH:
                MerchantTaskOperatorCallbackDTO dtoFinish =
                        JSON.parseObject(json,MerchantTaskOperatorCallbackDTO.class);
                merchantToTaskCenterServiceInterface.riderFinishOrder(dtoFinish.getTaskNo(),dtoFinish.getOperateTime());
                break;
            case EXPIRED:
                MerchantTaskOperatorCallbackDTO dtoExpire =
                        JSON.parseObject(json,MerchantTaskOperatorCallbackDTO.class);
                merchantToTaskCenterServiceInterface.orderExpire(dtoExpire.getTaskNo(),dtoExpire.getOperateTime());
                break;
            case ADMIN_CANCEL:
                MerchantTaskOperatorCallbackDTO dtoCancel =
                        JSON.parseObject(json,MerchantTaskOperatorCallbackDTO.class);
                merchantToTaskCenterServiceInterface.adminCancel(dtoCancel.getTaskNo(),dtoCancel.getOperateTime());
                break;
        }

    }
}
