package com.xinguang.tubobo.impl.merchant.service;

import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.dao.MerchantCompensateFeeConfigDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantCompensateFeeConfigEntity;
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
public class MerchantCompensateFeeConfigService extends BaseService{
    @Autowired private MerchantCompensateFeeConfigDao compensateFeeConfigDao;

    /**
     * 查询列表
     */
    @Cacheable(value = RedisCache.MERCHANT, key = "'merchantCompensate_all'")
    @Transactional(readOnly = true)
    public List<MerchantCompensateFeeConfigEntity> findAll(){
        return compensateFeeConfigDao.findAll();
    }

    /**
     * 删除列表
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantCompensate_*'")
    @Transactional
    public int deleteAll(){
        return compensateFeeConfigDao.deleteAllData();
    }

    /**
     * 保存列表
     */
    @CacheEvict(value = RedisCache.MERCHANT, key = "'merchantCompensate_*'")
    @Transactional
    public void saveAll(List<MerchantCompensateFeeConfigEntity> list){
        compensateFeeConfigDao.save(list);
    }
}
