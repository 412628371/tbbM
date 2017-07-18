package com.xinguang.tubobo.impl.merchant.dao;

import com.hzmux.hzcms.common.persistence.BaseDao;
import com.hzmux.hzcms.common.persistence.Page;
import com.hzmux.hzcms.common.persistence.Parameter;
import com.hzmux.hzcms.common.utils.StringUtils;
import com.xinguang.tubobo.impl.merchant.entity.BaseMerchantEntity;
import com.xinguang.tubobo.impl.merchant.entity.PushNoticeEntity;
import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by xuqinghua on 2017/7/13.
 */
@Repository
@Transactional(readOnly = true)
public class PushNoticeDao extends BaseDao<PushNoticeEntity> {
    /**
     * 保存通知记录
     * @param entity
     */
    @Transactional()
    public void saveEntity(PushNoticeEntity entity){
        entity.setCreateDate(new Date());
        entity.setProcessed(false);
        entity.setUpdateDate(new Date());
        entity.setDelFlag(BaseMerchantEntity.DEL_FLAG_NORMAL);
        save(entity);
    }


    /**
     * 将通知标记为已读
     * @param userId
     * @param id
     * @param processed 设置是否已读，true:已读，false:未读
     * @param delFlag 设置是否删除标记位，0：不删除，1：删除
     * @return
     */
    @Transactional()
    public boolean processNotice(String userId,long id,boolean processed,String delFlag){
        String hql = "update PushNoticeEntity set processed=:processed ,updateDate=:updateDate where id=:id  and userId=:userId and delFlag=:delFlag ";
        Parameter parameter = new Parameter();
        parameter.put("id",id);
        parameter.put("userId",userId);
        parameter.put("updateDate",new Date());
        parameter.put("processed",processed);
        parameter.put("delFlag",delFlag);
        int count = update(hql,parameter);
        return count == 1;
    }


    /**
     *
     * @param userId
     * @param processStatus 处理状态：2-未读，1：已读，0：全部
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Transactional()
    public Page<PushNoticeEntity> findNoticePage(String userId, int processStatus, int pageNo, int pageSize){
        StringBuffer hqlSb = new StringBuffer("select * from t_notice_entity where   del_flag='0' ");
        Parameter parameter = new Parameter();
        if (StringUtils.isNotBlank(userId)){
            hqlSb.append(" and user_id=:userId ");
            parameter.put("userId",userId);
        }
        if (processStatus > 0){
            boolean processed = false;
            if (processStatus == 1){
                processed = true;
            }
            parameter.put("processed",processed);
        }

        hqlSb.append(" order by id desc");
        return findPage(hqlSb.toString(), parameter, PushNoticeEntity.class,pageNo,pageSize);
    }
}
