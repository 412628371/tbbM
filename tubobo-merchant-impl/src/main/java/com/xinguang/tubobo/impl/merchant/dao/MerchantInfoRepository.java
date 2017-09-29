package com.xinguang.tubobo.impl.merchant.dao;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Created by yanxu on 2017/9/29.
 */
    public interface MerchantInfoRepository extends JpaRepository<MerchantInfoEntity, String>, JpaSpecificationExecutor<MerchantInfoEntity> {

        MerchantInfoEntity  findByUserId(String userId);

}
