package com.xinguang.tubobo.impl.merchant.thirdpart;

import com.alibaba.fastjson.JSON;
import com.xinguang.tubobo.impl.merchant.entity.ThirdMtOrderEntity;
import com.xinguang.tubobo.impl.merchant.service.MerchantThirdBindService;
import com.xinguang.tubobo.impl.merchant.service.ThirdMtOrderService;
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
public class MtCallbackHandler {
    @Autowired
    MerchantThirdBindService thirdBindService;
    @Autowired
    ThirdMtOrderService mtOrderService;
    public void dispatch(TakeoutNotifyConstant.MtNotifyType notifyType,String merchantId,String jsonContent){
        switch (notifyType){
            case STORE_BIND:
                MtStoreMapDTO mtStoreMapDTO = JSON.parseObject(jsonContent,MtStoreMapDTO.class);
                thirdBindService.bindMt(merchantId,mtStoreMapDTO.getAppAuthToken());
                break;
            case STORE_UNBIND:
//                MtStoreMapDTO unbindDTO = JSON.parseObject(jsonContent,MtStoreMapDTO.class);
                thirdBindService.unbindMt(merchantId);
                break;
            case ORDER_CONFIRM:
                MtOrderDTO mtOrderDTO = JSON.parseObject(jsonContent,MtOrderDTO.class);
                if (null != mtOrderDTO){
                    ThirdMtOrderEntity mtOrderEntity = new ThirdMtOrderEntity();
                    BeanUtils.copyProperties(mtOrderDTO,mtOrderEntity);
                    mtOrderEntity.setUserId(merchantId);
                    mtOrderEntity.setPlatformCode(TakeoutNotifyConstant.PlatformCode.MT.getValue());
                    mtOrderService.saveMtOrder(mtOrderEntity);
                }
                break;
            default:
                break;
        }
    }
}
