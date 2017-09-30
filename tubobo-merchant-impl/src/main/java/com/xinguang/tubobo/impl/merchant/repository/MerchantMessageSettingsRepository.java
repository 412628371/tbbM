package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by yangxb on 2017/9/30.
 */
public interface MerchantMessageSettingsRepository extends JpaRepository<MerchantMessageSettingsEntity, String>, JpaSpecificationExecutor<MerchantMessageSettingsEntity> {

    MerchantMessageSettingsEntity findByUserIdAndDelFlag(String userId, String delFlag);

}
