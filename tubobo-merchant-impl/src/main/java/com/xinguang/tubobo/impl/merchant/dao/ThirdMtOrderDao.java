package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantThirdBindEntity;
import com.xinguang.tubobo.impl.merchant.entity.ThirdMtOrderEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by xuqinghua on 2017/7/1.
 */
@Repository
public class ThirdMtOrderDao extends BaseDao<ThirdMtOrderEntity> {
    /**
     * 根据原始订单号查找记录
     * @param originOrderId
     * @return
     */
    public ThirdMtOrderEntity findByOriginId(String originOrderId){
        String hql = "from ThirdMtOrderEntity where originOrderId=:originOrderId and delFlag='0'";
        Parameter parameter = new Parameter();
        parameter.put("originOrderId",originOrderId);
        ThirdMtOrderEntity entity = getByHql(hql,parameter);
        return entity;
    }

    public Page<ThirdMtOrderEntity> findUnProcessedPageByUserId(String userId,int pageNo, int pageSize){
        String hql = "select * from t_third_mt_order where user_id=:userId and processed=:processed and del_flag='0'";
        Parameter parameter = new Parameter();
        parameter.put("userId",userId);
        parameter.put("processed",false);
        return findPage(hql, parameter, ThirdMtOrderEntity.class,pageNo,pageSize);
    }


    /**
     * 将订单标记为已处理
     * @param originOrderId
     */
    public boolean processOrder(String userId,String originOrderId){
        String hql = "update ThirdMtOrderEntity set processed=:processed where originOrderId=:originOrderId and userId=:userId and delFlag='0'";
        Parameter parameter = new Parameter();
        parameter.put("originOrderId",originOrderId);
        parameter.put("userId",userId);
        parameter.put("processed",true);
        int count = update(hql,parameter);
        return count == 1;
    }
}
