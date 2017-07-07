package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.impl.merchant.dao.ThirdOrderDao;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/1.
 */
@Service
@Transactional(readOnly = true)
public class ThirdOrderService {
    Logger logger = LoggerFactory.getLogger(ThirdOrderService.class);

    @Autowired private ThirdOrderDao mtOrderDao;
    @Transactional()
    public void saveMtOrder(ThirdOrderEntity mtOrderEntity){
        ThirdOrderEntity existEntity = mtOrderDao.findByOriginId(mtOrderEntity.getOriginOrderId());
        if (null == existEntity){
            mtOrderEntity.setCreateDate(new Date());
            mtOrderEntity.setUpdateDate(new Date());
            mtOrderEntity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
            mtOrderDao.save(mtOrderEntity);
        }
    }
    @Transactional()
    public void processOrder(String userId,String platformCode,String originOrderId){
        mtOrderDao.processOrder(userId,platformCode,originOrderId);
    }

    public Page<ThirdOrderEntity> findUnProcessedPageByUserId(String userId, String platformCode,
                                                              String keyword,int pageNo, int pageSize){
        return mtOrderDao.findUnProcessedPageByUserId(userId,platformCode,keyword,pageNo,pageSize);
    }

    /**
     * 逻辑删除第三方平台记录
     */
    @Transactional()
    public void delRecordsPastHours(int pastHours){
        int count = mtOrderDao.delRecordsPastHours(pastHours);
        logger.info("删除 {} 条,{} 小时之前的第三方平台订单记录",count,pastHours);
    }
}
