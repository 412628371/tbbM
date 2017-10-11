package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;
import com.xinguang.tubobo.impl.merchant.dao.MessageRecordDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageRecordEntity;
import com.xinguang.tubobo.rate.api.TbbRateResponse;
import com.xinguang.tubobo.rate.api.hystrix.TbbRateServiceHystrixProxy;
import com.xinguang.tubobo.rate.api.resp.RateContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 评价服务.
 */
@Service
public class MessageRecordService {
    Logger logger = LoggerFactory.getLogger(MessageRecordService.class);
    @Autowired
    private MessageRecordDao messageRecordDao;


    public void saveEntity(MerchantMessageRecordEntity merchantMessageRecordEntity){
        messageRecordDao.save(merchantMessageRecordEntity);
    }
}
