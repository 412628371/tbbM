package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.GeoLocation;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDTO;

/**
 * 任务中心提供给商家端的dubbo接口
 */
public interface TaskCenterToMerchantServiceInterface {

    /**
     * 商家下单
     * @param dto
     */
    boolean merchantOrder(MerchantOrderDTO dto);

    /**
     * 商家取消订单
     * @param orderNo
     * @return
     */
    boolean meachantCancelOrder(String orderNo);

    /**
     * 根据骑手id获取骑手的当前位置
     * @param riderId
     * @return
     */
    GeoLocation getRiderLocation(String riderId);
}
