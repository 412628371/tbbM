package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Created by yangxb on 2017/9/30.
 */
public interface ThirdOrderRepository  extends JpaRepository<ThirdOrderEntity, Long>, JpaSpecificationExecutor<ThirdOrderEntity> {
    /**
     * 根据原始订单号查找记录
     * @param originOrderId
     * @return
     */
     ThirdOrderEntity findByOriginOrderIdAndPlatformCode(String originOrderId, String platformCode);

    /**
     * 将订单标记为已处理
     * @param originOrderId
     */
    @Modifying
    @Query("update #{#entityName} a set a.processed =:processed, a.updateDate=:updateDate where a.originOrderId =:originOrderId and a.platformCode =:platformCode and a.userId=:userId  and a.delFlag =:whereDelFlag ")
    int processOrder(@Param("processed") boolean processed, @Param("updateDate") Date updateDate, @Param("originOrderId") String originOrderId,
                     @Param("platformCode") String platformCode, @Param("userId") String userId,@Param("whereDelFlag")String whereDelFlag);

    /**
     * 逻辑删除所有的记录，即标志位变为删除状态
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.delFlag =:delFlag, a.updateDate=:updateDate where a.delFlag =:whereDelFlag and a.createDate < :lastValidDate")
    int delRecordsPastHours(@Param("delFlag")String delFlag,@Param("updateDate") Date updateDate,@Param("lastValidDate") Date lastValidDate,@Param("whereDelFlag")String whereDelFlag);
}
