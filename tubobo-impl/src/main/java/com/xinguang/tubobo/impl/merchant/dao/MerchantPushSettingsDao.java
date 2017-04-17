package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */
@Repository
public class MerchantPushSettingsDao extends BaseDao<MerchantSettingsEntity> {
    /**
     * 更新设置,如果设置不存在则先插入
     * @param entity
     * @return
     */
    public int updatePushSettings(MerchantSettingsEntity entity){
        MerchantSettingsEntity existEntity = findByUserId(entity.getUserId());
        if (existEntity == null){
            existEntity = new MerchantSettingsEntity(entity.getUserId());
            existEntity.setPushMsgOrderExpired(true);
            existEntity.setPushMsgOrderFinished(true);
            existEntity.setPushMsgOrderGrabed(true);
            save(entity);
        }
        StringBuilder hqlSb = new StringBuilder("update MerchantSettingsEntity t set t.updateDate=:updateDate ");
        if (entity.isPushMsgOrderExpired()){
            hqlSb.append("t.pushMsgOrderExpired = :expired ");
        }
        if (entity.isPushMsgOrderFinished()){
            hqlSb.append("t.pushMsgOrderFinished = :finished ").append(entity.isPushMsgOrderFinished());
        }
        if (entity.isPushMsgOrderGrabed()){
            hqlSb.append("t.pushMsgOrderGrabed = :grabed ").append(entity.isPushMsgOrderGrabed());
        }
        hqlSb.append(" where userId = :userId ").append(" and delFlag=:delFlag ");
        Query query = getSession().createQuery(hqlSb.toString());
        query.setParameter("updateDate",new Date());
        query.setParameter("expired",entity.isPushMsgOrderExpired());
        query.setParameter("finished",entity.isPushMsgOrderFinished());
        query.setParameter("grabed",entity.isPushMsgOrderGrabed());
        query.setParameter("delFlag",entity.getDelFlag());
        query.setParameter("userId",entity.getUserId());
        int result = query.executeUpdate();
        getSession().clear();
        return  result;
    }
    public MerchantSettingsEntity findByUserId(String orderNo){
        String sqlString = "select * from tubobo_merchant_settings where userId = :p1 and del_flag = '0' ";
        List<MerchantSettingsEntity> list = findBySql(sqlString, new Parameter(orderNo), MerchantSettingsEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }
}
