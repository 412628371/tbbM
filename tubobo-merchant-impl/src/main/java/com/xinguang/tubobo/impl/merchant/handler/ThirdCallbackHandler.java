package com.xinguang.tubobo.impl.merchant.handler;

import com.alibaba.fastjson.JSON;
import com.xinguang.tubobo.impl.merchant.entity.ThirdOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantThirdBindService;
import com.xinguang.tubobo.impl.merchant.service.ThirdOrderService;
import com.xinguang.tubobo.takeout.TakeoutNotifyConstant;
import com.xinguang.tubobo.takeout.mt.MtOrderDTO;
import com.xinguang.tubobo.takeout.mt.MtStoreMapDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xuqinghua on 2017/6/30.
 */
@Service
public class ThirdCallbackHandler {
    @Autowired
    MerchantThirdBindService thirdBindService;
    @Autowired
    ThirdOrderService mtOrderService;
    public void dispatch(TakeoutNotifyConstant.PlatformCode platformCode,
                         TakeoutNotifyConstant.MtNotifyType notifyType, String merchantId, String jsonContent){
        switch (notifyType){
            case STORE_BIND:
                MtStoreMapDTO mtStoreMapDTO = JSON.parseObject(jsonContent,MtStoreMapDTO.class);
                thirdBindService.bindMt(platformCode,merchantId,mtStoreMapDTO.getAppAuthToken());
                break;
            case STORE_UNBIND:
                thirdBindService.unbindMt(platformCode,merchantId);
                break;
            case ORDER_CONFIRM:
                MtOrderDTO mtOrderDTO = JSON.parseObject(jsonContent,MtOrderDTO.class);
                if (null != mtOrderDTO){
                    ThirdOrderEntity mtOrderEntity = new ThirdOrderEntity();
                    BeanUtils.copyProperties(mtOrderDTO,mtOrderEntity);
                    mtOrderEntity.setUserId(merchantId);
                    mtOrderEntity.setPlatformCode(platformCode.getValue());
                    mtOrderService.saveMtOrder(mtOrderEntity);
                }
                break;
            default:
                break;
        }
    }
}
