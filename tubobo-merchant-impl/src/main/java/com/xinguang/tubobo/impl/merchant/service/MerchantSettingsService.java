package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.dao.MerchantPushSettingsDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/4/15.
 */
@Service
public class MerchantSettingsService {
    @Autowired
    MerchantPushSettingsDao settingsDao;

    /**
     * 设置Settings
     * @param entity
     * @return
     */
    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantSettings_'+#userId")
    public boolean updateSettings(MerchantSettingsEntity entity){
        int count = settingsDao.updatePushSettings(entity);
        return count == 1;
    }

    /**
     * 根据userId找设置
     * @param userId
     * @return
     */
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantSettings_'+#userId")
    public MerchantSettingsEntity findBuUserId(String userId){
        return settingsDao.findByUserId(userId);
    }
}
