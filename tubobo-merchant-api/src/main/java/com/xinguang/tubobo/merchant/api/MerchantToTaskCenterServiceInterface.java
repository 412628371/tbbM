package com.xinguang.tubobo.merchant.api;

import java.util.Date;

/**
 * 商家端提供给任务中心的dubbo接口
 */
public interface MerchantToTaskCenterServiceInterface {

    /**
     * 骑手抢单
     * @param riderId
     * @param riderName
     * @param riderPhone
     * @param orderNo
     * @param grabOrderTime
     */
    public boolean riderGrabOrder(String riderId,String riderName,String riderPhone,String orderNo,Date grabOrderTime);

    /**
     * 骑手取货
     * @param orderNo
     * @param grabItemTime
     */
    public boolean riderGrabItem(String orderNo,Date grabItemTime);

    /**
     * 骑手完成任务
     * @param orderNo
     * @param finishOrderTime
     */
    public boolean riderFinishOrder(String orderNo,Date finishOrderTime);

    /**
     * 订单超时
     * @param orderNo
     */
    public boolean orderExpire(String orderNo);

}
