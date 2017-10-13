package com.xinguang.tubobo.impl.merchant.repository;

import com.xinguang.tubobo.impl.merchant.entity.MerchantTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by lvhantai on 2017/9/30.
 */
public interface MerchantTypeRepository extends JpaRepository<MerchantTypeEntity, Long>, JpaSpecificationExecutor<MerchantTypeEntity> {
    List<MerchantTypeEntity> findAllByDelFlagOrderOrderByName(String delFlag);
    MerchantTypeEntity findByIdAndDelFlag(Long id, String delFlag);
    MerchantTypeEntity findByNameAndDelFlag(String name, String delFlag);

    /**
     * 修改模板名称
     * @param temName
     * @param temId
     * @param delFlag
     * @return
     */
    @Modifying
    @Query("update #{#entityName} a set a.temName =:temName where a.temId =:temId and a.delFlag =:delFlag ")
    int updateTemName(@Param("temName")String temName, @Param("temId")Long temId, @Param("delFlag")String delFlag);




}
