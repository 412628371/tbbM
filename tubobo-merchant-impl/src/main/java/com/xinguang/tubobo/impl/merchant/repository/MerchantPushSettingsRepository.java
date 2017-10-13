package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * Created by Administrator on 2017/4/15.
 */
public interface MerchantPushSettingsRepository extends JpaRepository<MerchantSettingsEntity, String>, JpaSpecificationExecutor<MerchantSettingsEntity> {
    MerchantSettingsEntity findByUserIdAndDelFlag(String userId,String delFlag);


 /* *//**
     * 更新设置,如果设置不存在则先插入
     * @param entity
     * @return
     *//*
    @Transactional(readOnly = false)
    public int updatePushSettings(MerchantSettingsEntity entity){
        MerchantSettingsEntity existEntity = findByUserId(entity.getUserId());
        if (existEntity == null){
            existEntity = new MerchantSettingsEntity();
            existEntity.setPushMsgOrderExpired(true);
            existEntity.setPushMsgOrderFinished(true);
            existEntity.setPushMsgOrderGrabed(true);
            existEntity.setPushMsgVoiceOpen(true);
            save(entity);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("updateDate",new Date());

        StringBuilder hqlSb = new StringBuilder("update MerchantSettingsEntity t set t.updateDate=:updateDate ");
        if (null != entity.getPushMsgOrderExpired()){
            hqlSb.append(", t.pushMsgOrderExpired = :expired ");
            map.put("expired",entity.getPushMsgOrderExpired());
        }
        if (null !=entity.getPushMsgOrderFinished()){
            hqlSb.append(", t.pushMsgOrderFinished = :finished ");
            map.put("finished",entity.getPushMsgOrderFinished());
        }
        if (null !=entity.getPushMsgOrderGrabed()){
            hqlSb.append(", t.pushMsgOrderGrabed = :grabed ");
            map.put("grabed",entity.getPushMsgOrderGrabed());
        }
        if (null !=entity.getPushMsgVoiceOpen()){
            hqlSb.append(", t.pushMsgVoiceOpen = :open ");
            map.put("open",entity.getPushMsgVoiceOpen());
        }
//        if (entity.getDeviceToken() != null){
//            hqlSb.append(", t.deviceToken = :deviceToken ");
//            map.put("deviceToken",entity.getDeviceToken());
//        }
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
    public MerchantSettingsEntity findByUserId(String userId){
        String sqlString = "select * from tubobo_merchant_settings where user_id = :p1 and del_flag = '0' ";
        List<MerchantSettingsEntity> list = findBySql(sqlString, new Parameter(userId), MerchantSettingsEntity.class);
        if (list != null && list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }*/
}
