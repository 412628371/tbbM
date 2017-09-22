package com.xinguang.tubobo.impl.merchant.common;

import com.hzmux.hzcms.common.utils.DateUtils;
import com.xinguang.tubobo.merchant.api.MerchantClientException;
import com.xinguang.tubobo.merchant.api.enums.EnumAuthentication;
import com.xinguang.tubobo.merchant.api.enums.EnumRespCode;

/**
 * Created by yangxb on 2017/9/19.
 */
public class OrderUtil {


    /**
     * 根据审核状态和上下班时间判断是否有权限发单
     * @param status 审核状态
     * @param beginWorkTime 开始工作时间
     * @param endWorkTime 结束工作时间
     * @throws MerchantClientException
     */
    public static void judgeOrderCondition(String status,String beginWorkTime,String endWorkTime,boolean isBigOrder) throws MerchantClientException {
        if (!EnumAuthentication.SUCCESS.getValue().equals(status)){
            throw new MerchantClientException(EnumRespCode.MERCHANT_STATUS_CANT_OPERATE);
        }
        if (isBigOrder){
            if (!DateUtils.isAfterBeginTimeInOneDay(beginWorkTime)||
                    !DateUtils.isBeforeEndTimeInOneDay(endWorkTime)){
                throw new MerchantClientException(EnumRespCode.CONSIGNOR_NOT_WORK);
            }
        }else {
            if (!DateUtils.isAfterBeginTimeInOneDay(beginWorkTime)||
                    !DateUtils.isBeforeEndTimeInOneDay(endWorkTime)){
                throw new MerchantClientException(EnumRespCode.MERCHANT_NOT_WORK);
            }
        }

    }
}

