package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantThirdBindEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by xuqinghua on 2017/6/29.
 */
@Repository
public class MerchantThirdBindDao extends BaseDao<MerchantThirdBindEntity> {
    public void updateMtBindInfo(String userId,boolean bound,String authToken){
        MerchantThirdBindEntity entity = findByUserId(userId);
        if (null == entity){
            entity = new MerchantThirdBindEntity();
            entity.setUserId(userId);
        }
        entity.setMtBound(bound);
        entity.setMtAuthToken(authToken);
        entity.setUpdateDate(new Date());
        save(entity);
    }
    public void updateEleBindInfo(String userId,boolean bound,String authToken){
        MerchantThirdBindEntity entity = findByUserId(userId);
        if (null == entity){
            entity = new MerchantThirdBindEntity();
            entity.setUserId(userId);
        }
        entity.setEleBound(bound);
        entity.setUpdateDate(new Date());
        save(entity);
    }
    public MerchantThirdBindEntity findByUserId(String userId){
        String hql = "from MerchantThirdBindEntity where userId=:userId and delFlag='0'";
        Parameter parameter = new Parameter();
        parameter.put("userId",userId);
        MerchantThirdBindEntity entity = getByHql(hql,parameter);
        return entity;
    }
}
