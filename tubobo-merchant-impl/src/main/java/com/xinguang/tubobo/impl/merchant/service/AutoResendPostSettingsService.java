package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.entity.AutoResendPostSettingEntity;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import com.xinguang.tubobo.impl.merchant.repository.AutoResendPostOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by yanx on 2017/9/15.
 */
@Service
public class AutoResendPostSettingsService {
    @Autowired
    AutoResendPostOrderRepository resendPostOrderRepository;

    /**
     * 驿站自动发单设置 Settings
     * @param entity
     * @return
     */
    @CacheEvict(value= RedisCache.MERCHANT,key="'autoResendPostSettings_'+#userId")
    public boolean updateSettings(String userId,AutoResendPostSettingEntity entity){
        AutoResendPostSettingEntity existEntity = resendPostOrderRepository.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        if (existEntity == null){
            existEntity = new AutoResendPostSettingEntity();
        }
        existEntity.setAutoPostOrderResendOpen(entity.getAutoPostOrderResendOpen());
        existEntity.setUserId(userId);
        resendPostOrderRepository.save(existEntity);
        return true;
    }
    /**
     * 驿站自动发单设置 根据userId找设置 null则插入数据
     * @param userId
     * @return
     */
    @Cacheable(value= RedisCache.MERCHANT,key="'autoResendPostSettings_'+#userId")
    public AutoResendPostSettingEntity findByUserIdAndCreate(String userId){
        AutoResendPostSettingEntity exist = resendPostOrderRepository.findByUserIdAndDelFlag(userId, MerchantMessageSettingsEntity.DEL_FLAG_NORMAL);
       if (exist==null){
           exist = new AutoResendPostSettingEntity();
           exist.setAutoPostOrderResendOpen(false);
           exist.setUserId(userId);
           updateSettings(userId,exist);
           exist= resendPostOrderRepository.findByUserIdAndDelFlag(userId, MerchantMessageSettingsEntity.DEL_FLAG_NORMAL);
       }
        return exist;
    }
}
