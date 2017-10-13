package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

/**
 * Created by lvhantai on 2017/9/30.
 */
public interface MerchantInfoRepository extends JpaRepository<MerchantInfoEntity, String>, JpaSpecificationExecutor<MerchantInfoEntity> {
    MerchantInfoEntity  findByUserIdAndDelFlag(String userId, String delFlag);

    @Modifying
    @Query("update #{#entityName} a set a.enablePwdFree = :enablePwdFree where a.userId = :userId and a.delFlag = :delFlag ")
    int freePayPwdSet(@Param("userId") String userId, @Param("delFlag") String delFlag, @Param("enablePwdFree") boolean enablePwdFree);

    @Modifying
    @Query("update #{#entityName} a set a.hasSetPayPwd = :hasSetPayPwd where a.userId = :userId and a.delFlag = :delFlag ")
    int modifyPwdSetFlag(@Param("userId") String userId, @Param("delFlag") String delFlag, @Param("hasSetPayPwd") boolean hasSetPayPwd);

    @Modifying
    @Query("update #{#entityName} a set a.merchantStatus = :merchantStatus, a.consignorStatus=:consignorStatus, a.updateDate=:updateDate,a.updateBy=:updateBy, " +
            " a.verifyDate=:verifyDate, a.reason=:reason where a.userId = :userId and a.delFlag = :delFlag ")
    int updateVerifyStatus(@Param("userId") String userId, @Param("delFlag") String delFlag, @Param("merchantStatus") String merchantStatus, @Param("consignorStatus") String consignorStatus,
                           @Param("updateDate") Date updateDate, @Param("verifyDate") Date verifyDate,@Param("updateBy") String updateBy, @Param("reason") String reason);

    @Modifying
    @Query("update #{#entityName} a set a.bdCode = :bdCode, a.bdUpdateDate=:bdUpdateDate, a.updateDate=:updateDate where a.userId = :userId and a.delFlag = :delFlag ")
    int updateDBCode(@Param("userId") String userId, @Param("delFlag") String delFlag, @Param("bdCode") String bdCode, @Param("bdUpdateDate") Date bdUpdateDate, @Param("updateDate") Date updateDate);

    @Modifying
    @Query("update #{#entityName} a set a.providerId = :providerId, a.enterTime=:enterTime, a.bindStatus=:bindStatus,a.updateDate=:updateDate,a.providerName=:providerName,a.unbundleTime=:unbundleTime where a.userId = :userId and a.delFlag = :delFlag and a.bindStatus <>:oldBindStatus ")
    int bindProvider(@Param("userId") String userId, @Param("delFlag") String delFlag, @Param("providerId") Long providerId,@Param("providerName") String providerName,@Param("bindStatus") String bindStatus, @Param("oldBindStatus") String oldBindStatus,@Param("enterTime") Date enterTime, @Param("updateDate") Date updateDate, @Param("unbundleTime") Date unbundleTime);

    @Modifying
    @Query("update #{#entityName} a set a.bindStatus=:bindStatus,a.unbundleTime=:unbundleTime, a.updateDate=:updateDate where a.userId = :userId and a.providerId=:providerId and a.delFlag = :delFlag and a.bindStatus=:oldBindStatus ")
    int unbindProvider(@Param("userId") String userId, @Param("providerId") Long providerId ,@Param("delFlag") String delFlag,@Param("bindStatus") String bindStatus, @Param("oldBindStatus") String oldBindStatus, @Param("unbundleTime") Date unbundleTime, @Param("updateDate") Date updateDate);

    @Modifying
    @Query("update #{#entityName} a set a.avatarUrl = :avatarUrl, a.updateDate=:updateDate where a.userId = :userId and a.delFlag = :delFlag ")
    int updateHeadImage(@Param("userId") String userId, @Param("delFlag") String delFlag, @Param("avatarUrl") String avatarUrl, @Param("updateDate") Date updateDate);

}
