package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantMessageRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by yangxb on 2017/9/30.
 */
public interface MessageRecordRepository extends JpaRepository<MerchantMessageRecordEntity, String>, JpaSpecificationExecutor<MerchantMessageRecordEntity> {

   // MessageRecordRepository findByUserIdAndDelFlag(String userId, String delFlag);

}
