package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.MerchantDeliverFeeTemDTO;

import java.util.List;

/**
 * 商家端提供给后台的dubbo接口,用于后台修改和展示商家定价模板
 */
public interface MerchantDeliverFeeTemInterface {

    /**
     * 查询所有模板
     * @return
     */
    List<MerchantDeliverFeeTemDTO> findAll();

    /**
     *
     * @param id
     * @return
     */
    MerchantDeliverFeeTemDTO findById(Long id);
    /**
     *
     * @param name
     * @return
     */
    MerchantDeliverFeeTemDTO findByName(String name);

    /**
     * 修改模板信息
     * @param merchantDeliverFeeTemDTO
     * @return
     */
    Boolean update(MerchantDeliverFeeTemDTO merchantDeliverFeeTemDTO);

    /**
     * 删除模板
     * @param id
     * @return
     */
    Boolean delete(Long id);
    /**
     * 保存模板
     * @param merchantDeliverFeeTemDTO
     * @return
     */
    Boolean save(MerchantDeliverFeeTemDTO merchantDeliverFeeTemDTO);

}
