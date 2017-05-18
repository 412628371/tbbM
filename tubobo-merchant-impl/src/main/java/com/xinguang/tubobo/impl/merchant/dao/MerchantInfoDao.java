/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.cache.RedisCache;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MerchantInfoDao extends BaseDao<MerchantInfoEntity> {

    public int freePayPwdSet(String userId,boolean enable) {
        String sqlString = "update MerchantInfoEntity set enablePwdFree=:p1 where userId = :p2 and delFlag = '0' ";
        int count = createQuery(sqlString, new Parameter(enable,userId)).executeUpdate();
        return count;
    }
}
