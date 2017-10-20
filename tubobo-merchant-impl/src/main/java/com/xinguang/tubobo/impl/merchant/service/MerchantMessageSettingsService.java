package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantMessageSettingsRepository;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
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
     * 短信设置 根据userId找设置 为空则自动插入一条数据
     * @param userId
     * @return
     */
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantMessageSettings_'+#userId")
    public MerchantMessageSettingsEntity findByUserIdAndCreate(String userId){
        MerchantMessageSettingsEntity exist = messageRepository.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        if (exist==null){
            exist = new MerchantMessageSettingsEntity();
            exist.setMessageOpen(false);
            exist.setUserId(userId);
            updateSettings(userId,exist);
            exist= messageRepository.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        }
        return exist;
    }
}
