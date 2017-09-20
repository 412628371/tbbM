package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.condition.MerchantInfoQueryCondition;
import com.xinguang.tubobo.merchant.api.condition.MerchantOrderQueryCondition;
import com.xinguang.tubobo.merchant.api.dto.MerchantInfoDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;
import com.xinguang.tubobo.merchant.api.dto.PageDTO;
import com.xinguang.tubobo.merchant.api.enums.EnumMerchantPostExceptionCode;

/**
 * Created by xuqinghua on 2017/8/31.
 */
public interface MerchantToPostHouseServiceInterface {
    EnumMerchantPostExceptionCode bindProvider(String userId, Long providerId, String providerName);
    /**
     * 根据查询条件查询骑手数据
     * @param queryCondition
     * @return
     */
    PageDTO<MerchantInfoDTO> findMerchantList(MerchantInfoQueryCondition queryCondition);

    /**
     * 根据查询条件查询订单数据
     * @param queryCondition
     * @return
     */
    PageDTO<MerchantOrderDTO> findMerchantOrderList(MerchantOrderQueryCondition queryCondition);

}
