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
     * 查询不同类型的起送费全部数据
     * @param
     * @return
     */
    List<MerchantDeliverFeeConfigDTO> findByOrderType(String orderType);

    /**
     * 多类型查询条件下  查询数据
     * @param
     * @return
     */
    List<MerchantDeliverFeeConfigDTO> queryAllByCondition(MerchantDeliverFeeConfigDTO merchantDeliverFeeConfigDTO);


    /**
     * 根据区code查询具体区的费用
     * @return
     */
    List<MerchantDeliverFeeConfigDTO> findFeeByAreaCode(String areaCode);
    /**
     * 根据区code和订单类型查询具体区的费用
     * @return
     */
    List<MerchantDeliverFeeConfigDTO> findFeeByAreaCodeAndOrderTypeAndTemId(String areaCode,String orderType,Long temId);

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
    void clearAndSaveListByAreaCodeAndOrderTypeAndTemId(List<MerchantDeliverFeeConfigDTO> list);

}
