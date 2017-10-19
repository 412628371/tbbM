package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.AutoResendPostSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by yangxb on 2017/9/30.
 */
public interface AutoResendPostOrderRepository extends JpaRepository<AutoResendPostSettingEntity, String>, JpaSpecificationExecutor<AutoResendPostSettingEntity> {

    AutoResendPostSettingEntity findByUserIdAndDelFlag(String userId, String delFlag);

}
