package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.MerchantTypeDTO;

import java.util.List;

/**
 * 商家端提供给后台的dubbo接口,用于后台修改和展示商家定价模板
 */
public interface MerchantTypeInterface {

    /**
     * 查询所有模板
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
     * 修改模板信息
     * @param merchantTypeDTO
     * @return
     */
    Boolean update(MerchantTypeDTO merchantTypeDTO);

    /**
     * 删除模板
     * @param id
     * @return
     */
    Boolean delete(Long id);

    /**
     * 保存模板
     * @param merchantTypeDTO
     * @return
     */
    Boolean save(MerchantTypeDTO merchantTypeDTO);

}
