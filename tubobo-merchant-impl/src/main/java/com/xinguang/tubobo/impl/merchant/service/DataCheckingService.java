package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.checking.CheckingTypeEnum;
import com.xinguang.tubobo.checking.DataCheckingDTO;
import com.xinguang.tubobo.impl.merchant.mq.RmqDataCheckingProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuqinghua .
 */
@Service
public class DataCheckingService {
    @Autowired private RmqDataCheckingProducer producer;

    public void reportTaskCreateRpcException(String orderNo){
        sendToMq(new DataCheckingDTO(orderNo, CheckingTypeEnum.TASK_CREATE));
    }

    private void sendToMq(DataCheckingDTO dto){
        producer.sendMessage(dto);
    }
}
