package com.xinguang.tubobo.merchant.api;

/**
 * Created by yangxb on 2017/9/11.
 */

import com.xinguang.tubobo.merchant.api.dto.MerchantAbortConfirmDTO;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderCreateDto;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderCreateResultDto;
import com.xinguang.tubobo.merchant.api.dto.MerchantOrderDetailDTO;

/**
 * 商家提供给外部三方的接口
 */
public interface MerchantToThirdPartyServiceInterface {

    /**
     * 创建单一订单
     */
    MerchantOrderCreateResultDto createOrder(MerchantOrderCreateDto req);

    /**
     * 查看订单详情
     * @return
     */
    MerchantOrderDetailDTO orderDetail(String OrderNo,String userId);


    /**
     * 查看订单取消
     * @return
     */
    TbbMerchantResponse<Boolean> orderCancel(String OrderNo,String userId);

    /**
     *
     * @param req
     * @return
     */
    boolean abortConfirm(MerchantAbortConfirmDTO req);



}
