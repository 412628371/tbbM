package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantThirdBindEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;


/**
 * Created by xuqinghua on 2017/6/29.
 */
public interface MerchantThirdBindRepository extends JpaRepository<MerchantThirdBindEntity, String>, JpaSpecificationExecutor<MerchantThirdBindEntity> {

    MerchantThirdBindEntity findByUserIdAndDelFlag(String userId,String delFlag);


   /* @Modifying
    @Query("update #{#entityName} a set a.userId = :userId, a.mtBound=:mtBound,a.mtAuthToken=:mtAuthToken,a.updateDate=:updateDate,a.getTime=:getTime ,a.expectFinishTime=:expectFinishTime where a.orderNo = :orderNo and a.delFlag = :delFlag   ")
    int updateMtBindInfo(@Param("userId") String userId, @Param("mtBound") Boolean mtBound, @Param("mtAuthToken") String mtAuthToken,
                         @Param("updateDate") String updateDate,@Param("updateDate") String updateDate

                               , @Param("expectFinishTime") Date expectFinishTime)
                               */
    /*  public void updateMtBindInfo(String userId,boolean bound,String authToken){
        MerchantThirdBindEntity entity = findByUserId(userId);
        if (null == entity){
            entity = new MerchantThirdBindEntity();
            entity.setUserId(userId);
        }
        entity.setMtBound(bound);
        entity.setMtAuthToken(authToken);
        entity.setUpdateDate(new Date());
        save(entity);
    }
    public void updateEleBindInfo(String userId,boolean bound){
        MerchantThirdBindEntity entity = findByUserId(userId);
        if (null == entity){
            entity = new MerchantThirdBindEntity();
            entity.setUserId(userId);
        }
        entity.setEleBound(bound);
        entity.setUpdateDate(new Date());
        save(entity);
    }
    public void updateYzBindInfo(String userId,boolean bound){
        MerchantThirdBindEntity entity = findByUserId(userId);
        if (null == entity){
            entity = new MerchantThirdBindEntity();
            entity.setUserId(userId);
        }
        entity.setYzBound(bound);
        entity.setUpdateDate(new Date());
        save(entity);
    }
    public MerchantThirdBindEntity findByUserId(String userId){
        String hql = "from MerchantThirdBindEntity where userId=:userId and delFlag='0'";
        Parameter parameter = new Parameter();
        parameter.put("userId",userId);
        MerchantThirdBindEntity entity = getByHql(hql,parameter);
        return entity;
    }*/


}
