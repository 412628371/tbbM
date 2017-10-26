package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by lvhantai on 2017/9/30.
 */
public interface MerchantTypeRepository extends JpaRepository<MerchantTypeEntity, Long>, JpaSpecificationExecutor<MerchantTypeEntity> {
    List<MerchantTypeEntity> findAllByDelFlagOrderByIdDesc(String delFlag);
    MerchantTypeEntity findByIdAndDelFlag(Long id, String delFlag);
    MerchantTypeEntity findByNameAndDelFlag(String name, String delFlag);

}
