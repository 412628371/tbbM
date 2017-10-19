package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantMessageSettingsRepository;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yanx on 2017/9/15.
 */
@Service
public class MerchantMessageSettingsService {
    @Autowired
    MerchantMessageSettingsRepository messageRepository;

    /**
     * 短信设置 Settings
     * @param entity
     * @return
     */
    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantMessageSettings_'+#userId")
    public boolean updateSettings(String userId,MerchantMessageSettingsEntity entity){
        MerchantMessageSettingsEntity existEntity = messageRepository.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        if (existEntity == null){
            existEntity = new MerchantMessageSettingsEntity();
        }
        existEntity.setMessageOpen(entity.getMessageOpen());
        existEntity.setUserId(entity.getUserId());
        messageRepository.save(existEntity);
        return true;
    }
    /**
     * 短信设置 根据userId找设置
     * @param userId
     * @return
     */
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantMessageSettings_'+#userId")
    public MerchantMessageSettingsEntity findBuUserId(String userId){
        return messageRepository.findByUserIdAndDelFlag(userId,MerchantMessageSettingsEntity.DEL_FLAG_NORMAL);
    }
}