package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.PushNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by xuqinghua on 2017/7/13.
 */
public interface PushNoticeRepository extends JpaRepository<PushNoticeEntity, String>, JpaSpecificationExecutor<PushNoticeEntity> {



    /**
     * 将通知标记为已读
     * @param userId
     * @param id
     * @return
     */

    @Modifying
    @Query("update #{#entityName} a set a.processed =:processed where a.id =:id and a.userId =:userId and a.delFlag =:delFlag ")
    int processNotice(@Param("processed")Boolean processed, @Param("id")Long id, @Param("userId") String userId, @Param("delFlag")String delFlag);




    @Modifying
    @Query("update #{#entityName} a set a.delFlag =:delFlag where a.id =:id and a.userId =:userId and a.delFlag =:whereDelFlag ")
    int deleteNotice(@Param("delFlag")String delFlag, @Param("id")Long id, @Param("userId") String userId, @Param("whereDelFlag")String whereDelFlag);



    /**
     *
     * @param userId
     * @param processStatus 处理状态：2-未读，1：已读，0：全部
     * @param pageNo
     * @param pageSize
     * @return
     *//*
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
    }*/

    /**
     * 获取未读的系统通知数量
     * @param userId
     * @return
     */
    @Query(value ="select count(id) FROM t_notice_entity WHERE processed =:processed and user_id=:userId  and del_flag=:whereDelFlag  ", nativeQuery = true )
    Long getUnProcessedCount(@Param("processed")Boolean processed, @Param("userId") String userId, @Param("whereDelFlag")String whereDelFlag);




}
