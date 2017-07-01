package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.Page;
import com.xinguang.tubobo.impl.merchant.dao.ThirdMtOrderDao;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.ThirdMtOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/1.
 */
@Service
@Transactional(readOnly = true)
public class ThirdMtOrderService  {
    @Autowired private ThirdMtOrderDao mtOrderDao;
    @Transactional()
    public void saveMtOrder(ThirdMtOrderEntity mtOrderEntity){
        ThirdMtOrderEntity existEntity = mtOrderDao.findByOriginId(mtOrderEntity.getOriginOrderId());
        if (null == existEntity){
            mtOrderEntity.setCreateDate(new Date());
            mtOrderEntity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
            mtOrderDao.save(mtOrderEntity);
        }
    }
    @Transactional()
    public void processOrder(String userId,String originOrderId){
        mtOrderDao.processOrder(userId,originOrderId);
    }

    public Page<ThirdMtOrderEntity> findUnProcessedPageByUserId(String userId, int pageNo, int pageSize){
        return mtOrderDao.findUnProcessedPageByUserId(userId,pageNo,pageSize);
    }
}
