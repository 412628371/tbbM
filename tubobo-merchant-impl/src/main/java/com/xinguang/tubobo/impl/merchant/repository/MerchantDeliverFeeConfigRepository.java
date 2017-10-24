package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantDeliverFeeConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by lvhantai on 2017/9/30.
 */
public interface MerchantDeliverFeeConfigRepository extends JpaRepository<MerchantDeliverFeeConfigEntity, String>, JpaSpecificationExecutor<MerchantDeliverFeeConfigEntity> {
    List<MerchantDeliverFeeConfigEntity> findAllByDelFlagOrderByBeginDistance(String delFlag);
    List<MerchantDeliverFeeConfigEntity> findAllByDelFlagAndOrderTypeOrderByBeginDistance(String delFlag,String orderType);

    /**
     * 根据区查具体区的定价
     */
    List<MerchantDeliverFeeConfigEntity> findByAreaCodeAndDelFlagAndOrderTypeAndTemIdOrderByBeginDistance(String areaCode, String delFlag,String orderType,Long temId);

    @Modifying
    @Query("update #{#entityName} a set a.delFlag = :delFlag where a.areaCode = :areaCode and a.orderType =:orderType and a.temId = :temId")
    int deleteFeeByAreaCodeAndOrderType(@Param("delFlag") String delFlag, @Param("areaCode") String areaCode,@Param("orderType") String orderType,@Param("temId") Long temId);

}
