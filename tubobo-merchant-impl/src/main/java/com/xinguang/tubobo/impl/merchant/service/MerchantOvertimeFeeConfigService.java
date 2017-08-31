package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.dao.MerchantCompensateFeeConfigDao;
import com.xinguang.tubobo.impl.merchant.dao.MerchantOvertimeFeeConfigDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantCompensateFeeConfigEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOvertimeFeeConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lvhantai on 2017/8/31.
 */
@Service
public class MerchantOvertimeFeeConfigService extends BaseService{
    @Autowired private MerchantOvertimeFeeConfigDao overtimeFeeConfigDao;

    /**
     * 查询列表
     */
    @Cacheable(value = RedisCache.MERCHANT, key = "'merchantOvertime_all'")
    @Transactional(readOnly = true)
    public List<MerchantOvertimeFeeConfigEntity> findAll(){
        return overtimeFeeConfigDao.findAll();
    }

    /**
     * 删除列表
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOvertime_*'")
    @Transactional
    public int deleteAll(){
        return overtimeFeeConfigDao.deleteAllData();
    }

    /**
     * 保存列表
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantOvertime_*'")
    @Transactional
    public void saveAll(List<MerchantOvertimeFeeConfigEntity> list){
        overtimeFeeConfigDao.save(list);
    }
}
