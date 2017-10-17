package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantDeliverFeeTemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by lvhantai on 2017/9/30.
 */
public interface MerchantDeliverFeeTemRepository extends JpaRepository<MerchantDeliverFeeTemEntity, Long>, JpaSpecificationExecutor<MerchantDeliverFeeTemEntity> {
    List<MerchantDeliverFeeTemEntity> findAllByDelFlagOrderByIdDesc(String delFlag);
    MerchantDeliverFeeTemEntity findByIdAndDelFlag(Long id, String delFlag);
    MerchantDeliverFeeTemEntity findByNameAndDelFlag(String name, String delFlag);
}
