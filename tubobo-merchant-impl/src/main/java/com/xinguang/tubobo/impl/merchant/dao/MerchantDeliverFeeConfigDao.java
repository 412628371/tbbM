/**
 * Copyright &copy; 2012-2013 <a href="http://www.hzmux.com">hzmux</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantDeliverFeeConfigEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MerchantDeliverFeeConfigDao extends BaseDao<MerchantDeliverFeeConfigEntity> {

    public List<MerchantDeliverFeeConfigEntity> findAllFee(){
        String sqlString = "select * from tubobo_merchant_deliver_fee_config where del_flag = '0' order by begin_distance ";
        List<MerchantDeliverFeeConfigEntity> list = findBySql(sqlString, null, MerchantDeliverFeeConfigEntity.class);
        List<MerchantDeliverFeeConfigEntity> bySql = findBySql(sqlString);
        return list;
    }

    /**
     * 根据区查具体区的定价
     * @param areaCode
     * @return
     */
    public List<MerchantDeliverFeeConfigEntity> findFeeByAreaCode(String areaCode){
        String sqlString = "select * from tubobo_merchant_deliver_fee_config where del_flag = '0' and area_code = :p1 order by begin_distance ";
        List<MerchantDeliverFeeConfigEntity> list = findBySql(sqlString, new Parameter(areaCode), MerchantDeliverFeeConfigEntity.class);
        return list;
    }


    public boolean saveList(List<MerchantDeliverFeeConfigEntity> list){
        save(list);
        return true;
    }
    public int  deleteAllData(){
        String sqlString = "delete  from  tubobo_merchant_deliver_fee_config";
        int i = getSession().createSQLQuery(sqlString).executeUpdate();
        //int update = update(sqlString);
        return  i;
    }
}
