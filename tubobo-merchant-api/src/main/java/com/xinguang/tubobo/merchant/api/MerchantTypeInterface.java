package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;

import java.util.List;

/**
 * 商家端提供给后台的dubbo接口,用于后台修改和展示商家类型
 */
public interface MerchantTypeInterface {

    /**
     * 查询所有商家类型
     * @return
     */
    List<MerchantTypeDTO> findAll();

    /**
     *
     * @param id
     * @return
     */
    MerchantTypeDTO findById(Long id);
    /**
     *
     * @param name
     * @return
     */
    MerchantTypeDTO findByName(String name);


    /**
     * 删除模板
     * @param id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 保存商家类型
     * @param merchantTypeDTO
     * @return
     */
    Boolean save(MerchantTypeDTO merchantTypeDTO);

}
