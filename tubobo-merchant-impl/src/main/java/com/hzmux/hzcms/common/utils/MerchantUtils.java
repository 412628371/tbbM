package com.hzmux.hzcms.common.utils;

import com.xinguang.tubobo.account.api.trade.request.TradePayRequest;
import com.xinguang.tubobo.impl.merchant.common.MerchantConstants;

import java.util.UUID;

/**
 * Created by yanxu on 2017/12/9.
 */
public class MerchantUtils {
    /**
     * 构造accout 2.0 商家通用支付请求
     * */
    public static TradePayRequest.Builder getPayRequestBuilder(Long accountId, String orderNo, long amount) {
        TradePayRequest.TradePayRequestDetail tradePayRequestDetail = new TradePayRequest.TradePayRequestDetail(MerchantConstants.MERCHANT_PAY_REMARK, amount);
        return TradePayRequest.builder(UUID.randomUUID().toString().substring(10)+orderNo, amount, accountId, orderNo)
                .addOrderDetails(tradePayRequestDetail);
    }
}
