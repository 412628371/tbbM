package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageRecordEntity;
import com.xinguang.tubobo.impl.merchant.repository.MessageRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 保存消息流水
 */
@Service
@Transactional
public class MessageRecordService {
    Logger logger = LoggerFactory.getLogger(MessageRecordService.class);
    @Autowired
    private MessageRecordRepository messageRecordDao;


    public void saveEntity(MerchantMessageRecordEntity merchantMessageRecordEntity){
        messageRecordDao.save(merchantMessageRecordEntity);
        //messageRecordDao.save(merchantMessageRecordEntity);
    }
}
