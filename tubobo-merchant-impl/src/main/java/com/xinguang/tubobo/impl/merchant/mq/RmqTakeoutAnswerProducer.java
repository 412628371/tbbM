package com.xinguang.tubobo.impl.merchant.mq;

import com.alibaba.fastjson.JSON;
import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;
import com.xinguang.tubobo.takeout.answer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * Created by xuqinghua on 2017/7/17.
 */
@Service
public class RmqTakeoutAnswerProducer {
    private Logger logger = LoggerFactory.getLogger(RmqTakeoutAnswerProducer.class);
    @Autowired RabbitTemplate baseMqTemplate;
    public void sendAccepted(String platformCode, String userId, String orderNo,
                             String originOrderId, DispatcherInfoDTO dispatcherInfoDTO){
        TakeoutNotifyConstant.PlatformCode p = TakeoutNotifyConstant.PlatformCode.getByCode(platformCode);
        if (p == null) {
            return;
        }
        AcceptDTO acceptDTO = new AcceptDTO();
        acceptDTO.setOrderNo(orderNo);
        acceptDTO.setOriginOrderId(originOrderId);
        if (dispatcherInfoDTO!=null){
            acceptDTO.setDispatcherInfoDTO(dispatcherInfoDTO);
        }
        send(TakeoutNotifyConstant.ThirdAnswerType.ACCEPTED,p,userId,acceptDTO);
    }

    public void sendPicked(String platformCode, String userId, String orderNo,
                             String originOrderId, DispatcherInfoDTO dispatcherInfoDTO){
        TakeoutNotifyConstant.PlatformCode p = TakeoutNotifyConstant.PlatformCode.getByCode(platformCode);
        if (p == null) {
            return;
        }
        PickDTO pickDTO = new PickDTO();
        pickDTO.setOrderNo(orderNo);
        pickDTO.setOriginOrderId(originOrderId);
        if (dispatcherInfoDTO!=null){
            pickDTO.setDispatcherInfoDTO(dispatcherInfoDTO);
        }
        send(TakeoutNotifyConstant.ThirdAnswerType.PICKED,p,userId,pickDTO);
    }

    public void sendFinish(String platformCode, String userId, String orderNo,
                           String originOrderId, DispatcherInfoDTO dispatcherInfoDTO){
        TakeoutNotifyConstant.PlatformCode p = TakeoutNotifyConstant.PlatformCode.getByCode(platformCode);
        if (p == null) {
            return;
        }
        FinishDTO finishDTO = new FinishDTO();
        finishDTO.setOrderNo(orderNo);
        finishDTO.setOriginOrderId(originOrderId);
        if (dispatcherInfoDTO!=null){
            finishDTO.setDispatcherInfoDTO(dispatcherInfoDTO);
        }
        send(TakeoutNotifyConstant.ThirdAnswerType.FINISH,p,userId,finishDTO);
    }
    private void send(TakeoutNotifyConstant.ThirdAnswerType answerType,TakeoutNotifyConstant.PlatformCode platformCode,
                      String userId,Object data){

        ThirdAnswerDTO thirdAnswerDTO = new ThirdAnswerDTO();
        thirdAnswerDTO.setPlatformCode(platformCode);
        thirdAnswerDTO.setMerchantId(userId);
        thirdAnswerDTO.setAnswerType(answerType);

        thirdAnswerDTO.setJsonData(JSON.toJSONString(data));

        String msg = JSON.toJSONString(thirdAnswerDTO);
        MessageProperties messageProperties = new MessageProperties();
        Message message = null;
        try {
            message = new Message(msg.getBytes("utf-8"),messageProperties);
        } catch (UnsupportedEncodingException e) {
            logger.error("给第三方发送信息异常",e);
        }
        baseMqTemplate.convertAndSend(TakeoutNotifyConstant.RmqConfig.EXCHANGE_NAME.getValue(),
                TakeoutNotifyConstant.RmqConfig.THIRD_QUEUE_ANSWER_BINDING_KEY.getValue(),message);
    }
}
