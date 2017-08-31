package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantCompensateFeeConfigEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lvhantai on 2017/8/30.
 */
@Repository
public class MerchantCompensateFeeConfigDao extends BaseDao<MerchantCompensateFeeConfigEntity>{
    public List<MerchantCompensateFeeConfigEntity> findAll(){
        String sqlString = "select * from tubobo_merchant_compensate_fee_config where del_flag = '0'";
        List<MerchantCompensateFeeConfigEntity> list = findBySql(sqlString, null, MerchantCompensateFeeConfigEntity.class);
        return list;
    }

    public int  deleteAllData(){
        String sqlString = "delete from tubobo_merchant_compensate_fee_config";
        int i = getSession().createSQLQuery(sqlString).executeUpdate();
        return  i;
    }
}
