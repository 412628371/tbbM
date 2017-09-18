package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信开关设置
 * Created by yanx on 2017/9/18.
 */
@Repository
@Transactional(readOnly = true)
public class MerchantMessageSettingsDao extends BaseDao<MerchantMessageSettingsEntity> {
    /**
     * 更新设置,如果设置不存在则先插入
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public int updateSettings(MerchantMessageSettingsEntity entity){
        MerchantMessageSettingsEntity existEntity = findByUserId(entity.getUserId());
        if (existEntity == null){
            existEntity = new MerchantMessageSettingsEntity();
            existEntity.setMessageOpen(entity.getMessageOpen());
            entity.setUserId(entity.getUserId());
            save(entity);
            return 1;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("updateDate",new Date());
        StringBuilder hqlSb = new StringBuilder("update MerchantMessageSettingsEntity t set t.updateDate=:updateDate ");
        if (null !=entity.getMessageOpen()){
            hqlSb.append(", t.messageOpen = :open ");
            map.put("open",entity.getMessageOpen());
        }
        hqlSb.append(" where userId = :userId ").append(" and delFlag=:delFlag ");
        Query query = getSession().createQuery(hqlSb.toString());
        map.put("userId",entity.getUserId());
        map.put("delFlag",entity.DEL_FLAG_NORMAL);
        query.setProperties(map);
        int result = query.executeUpdate();
        getSession().clear();
        return  result;
    }
    public void deleteBuUserId(String userId){

    }

    public MerchantMessageSettingsEntity findByUserId(String userId){
        String sqlString = "select * from tubobo_merchant_message_settings where user_id = :p1 and del_flag = '0' ";
        List<MerchantMessageSettingsEntity> list = findBySql(sqlString, new Parameter(userId), MerchantMessageSettingsEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }
}
