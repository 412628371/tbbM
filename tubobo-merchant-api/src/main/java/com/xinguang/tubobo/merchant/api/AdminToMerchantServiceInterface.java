package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.CarTypeDTO;
import com.xinguang.tubobo.merchant.api.dto.CityOpenDTO;

import java.util.List;

/**
 * 兔波波后台提供给商家的服务接口.
 */
public interface AdminToMerchantServiceInterface {
    /**
     * 查询开通的城市列表
     * @param orderType 区分货主大件或者商家小件。
     * @see com.xinguang.tubobo.merchant.api.enums.EnumOrderType
     * @return
     */
    List<CityOpenDTO> queryOpenedCityList(String orderType);

    /**
     * 获取车辆类型列表
     * @return
     */
    List<CarTypeDTO> queryCarTypeList();

    /**
     * 根据车辆类型，获取车辆信息
     * @param carType
     * @return
     */
    CarTypeDTO queryCarTypeInfo(String carType);
}
