package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.xinguang.tubobo.impl.merchant.entity.MerchantCompensateFeeConfigEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOvertimeFeeConfigEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lvhantai on 2017/8/30.
 */
@Repository
public class MerchantOvertimeFeeConfigDao extends BaseDao<MerchantOvertimeFeeConfigEntity>{
    public List<MerchantOvertimeFeeConfigEntity> findAll(){
        String sqlString = "select * from tubobo_merchant_overtime_fee_config where del_flag = '0'";
        List<MerchantOvertimeFeeConfigEntity> list = findBySql(sqlString, null, MerchantOvertimeFeeConfigEntity.class);
        return list;
    }

    public int  deleteAllData(){
        String sqlString = "delete from tubobo_merchant_overtime_fee_config";
        int i = getSession().createSQLQuery(sqlString).executeUpdate();
        return  i;
    }
}
