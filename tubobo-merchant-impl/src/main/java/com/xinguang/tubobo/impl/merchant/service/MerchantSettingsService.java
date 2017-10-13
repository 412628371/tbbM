package com.xinguang.tubobo.impl.merchant.service;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.dao.MerchantPushSettingsDao;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import com.xinguang.tubobo.impl.merchant.repository.MerchantPushSettingsRepository;
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
    MerchantPushSettingsRepository settingsDao;

    /**
     * 设置Settings
     * @param entity
     * @return
     */
    @CacheEvict(value= RedisCache.MERCHANT,key="'merchantSettings_'+#userId")
    public boolean updateSettings(String userId,MerchantSettingsEntity entity){
       MerchantSettingsEntity settingsEntity = settingsDao.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
        if (null==settingsEntity){
            settingsEntity = new MerchantSettingsEntity();
        }
        settingsEntity.setPushMsgOrderExpired(entity.getPushMsgOrderExpired());
        settingsEntity.setPushMsgOrderFinished(entity.getPushMsgOrderFinished());
        settingsEntity.setPushMsgOrderGrabed(entity.getPushMsgOrderGrabed());
        settingsEntity.setPushMsgVoiceOpen(entity.getPushMsgVoiceOpen());
        MerchantSettingsEntity saveEntity = settingsDao.save(settingsEntity);
        return null==saveEntity?false:true;

    }

    /**
     * 根据userId找设置
     * @param userId
     * @return
     */
    @Cacheable(value= RedisCache.MERCHANT,key="'merchantSettings_'+#userId")
    public MerchantSettingsEntity findByUserId(String userId){
        return settingsDao.findByUserIdAndDelFlag(userId, BaseMerchantEntity.DEL_FLAG_NORMAL);
    }
}
