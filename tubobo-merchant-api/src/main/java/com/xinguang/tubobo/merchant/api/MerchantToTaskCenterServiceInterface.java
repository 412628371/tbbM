package com.xinguang.tubobo.merchant.api;

import com.xinguang.tubobo.merchant.api.dto.MerchantGrabCallbackDTO;

import java.util.Date;

/**
 * 商家端提供给任务中心的dubbo接口
 */
public interface MerchantToTaskCenterServiceInterface {

    /**
     * 骑手抢单
     */
     boolean riderGrabOrder(MerchantGrabCallbackDTO dto);

    /**
     * 骑手取货
     * @param orderNo
     * @param grabItemTime
     */
     boolean riderGrabItem(String orderNo,Date grabItemTime);

    /**
     * 骑手完成任务
     * @param orderNo
     * @param finishOrderTime
     */
     boolean riderFinishOrder(String orderNo,Date finishOrderTime);

    /**
     * 订单超时
     * @param orderNo
     */
     boolean orderExpire(String orderNo,Date expireTime);

    boolean adminCancel(String orderNo,Date expireTime) throws MerchantClientException;

    /**
     * 驿站订单 未妥投
     */
    boolean riderUnsettledOrder(String orderNo,String reason);

}
