package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.MerchantDeliverFeeConfigDTO;

import java.util.List;

/**
 * 商家端提供给后台的dubbo接口,用于后台修改和展示商家起送服务费
 */
public interface MerchantDeliverFeeConfigInterface {

    /**
     * 查询起送费全部数据
     * @param
     * @return
     */
    public List<MerchantDeliverFeeConfigDTO> findAll( );

    /**
     * 根据区code查询具体区的费用
     * @return
     */
    List<MerchantDeliverFeeConfigDTO> findFeeByAreaCode(String areaCode);

    /**
     * 保存商家起送费全部信息
     * @param list 保存的列表
     * @return
     */
    public void saveList(List<MerchantDeliverFeeConfigDTO> list);

    /**
     * 先清除然后保存商家起送费全部信息
     * @param  list 保存的列表
     * @return
     */
    public void clearAndSaveList(List<MerchantDeliverFeeConfigDTO> list);

    /**
     * 根据areaCode先清除然后保存指定区域商家起送费全部信息
     * @param list
     */
    void clearAndSaveListByAreaCode(List<MerchantDeliverFeeConfigDTO> list);
}
