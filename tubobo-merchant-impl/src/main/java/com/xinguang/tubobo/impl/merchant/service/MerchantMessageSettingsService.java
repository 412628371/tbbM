package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.dao.MerchantMessageSettingsDao;
import com.xinguang.tubobo.impl.merchant.dao.MerchantPushSettingsDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by yanx on 2017/9/15.
 */
@Service
public class MerchantMessageSettingsService {
    @Autowired
    MerchantMessageSettingsDao messageDao;

    /**
     * 短信设置 Settings
     * @param entity
     * @return
     */
    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantMessageSettings_'+#userId")
    public boolean updateSettings(String userId,MerchantMessageSettingsEntity entity){
        int count = messageDao.updateSettings(entity);
        return count == 1;
    }
    /**
     * 短信设置 根据userId找设置
     * @param userId
     * @return
     */
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantMessageSettings_'+#userId")
    public MerchantMessageSettingsEntity findBuUserId(String userId){
        return messageDao.findByUserId(userId);
    }
}
