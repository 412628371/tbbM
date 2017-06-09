/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MerchantInfoDao extends BaseDao<MerchantInfoEntity> {

    public MerchantInfoEntity findByUserId(String userId){
        if (StringUtils.isBlank(userId)) return null;
        String sqlString = "select * from tubobo_merchant_info where del_flag = '0' and user_id = :p1 ";
        List<MerchantInfoEntity> list = findBySql(sqlString, new Parameter(userId), MerchantInfoEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }
    public int freePayPwdSet(String userId,boolean enable) {
        String sqlString = "update MerchantInfoEntity set enablePwdFree=:p1 where userId = :p2 and delFlag = '0' ";
        int count = createQuery(sqlString, new Parameter(enable,userId)).executeUpdate();
        return count;
    }
    public int modifyPwdSetFlag(String userId) {
        String sqlString = "update MerchantInfoEntity set hasSetPayPwd=:p1 where userId = :p2 and delFlag = '0' ";
        int count = createQuery(sqlString, new Parameter(true,userId)).executeUpdate();
        return count;
    }
    public int updateVerifyStatus(String merchantStatus,String consignorStatus,String updateBy,String userId){

        String sqlString = "update MerchantInfoEntity set merchantStatus=:merchantStatus,consignorStatus=:consignorStatus,updateDate=:updateDate,updateBy=:updateBy where userId = :userId and delFlag = '0' ";
        Parameter parameter = new Parameter();
        parameter.put("merchantStatus",merchantStatus);
        parameter.put("consignorStatus",consignorStatus);
        parameter.put("updateBy",updateBy);
        parameter.put("userId",userId);
        parameter.put("updateDate",new Date());
        int count = createQuery(sqlString, parameter).executeUpdate();
        return count;
    }
}
