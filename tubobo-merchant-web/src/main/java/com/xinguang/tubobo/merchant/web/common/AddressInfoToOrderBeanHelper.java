package com.xinguang.tubobo.merchant.web.common;

import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;

/**
 * 将地址信息设置到实体的工具类.
 */
public class AddressInfoToOrderBeanHelper {
    public static MerchantOrderEntity putSenderFromAddressInfo(MerchantOrderEntity entity,AddressInfo sender){
        if (null == entity){
            entity = new MerchantOrderEntity();
        }
        entity.setSenderAddressDetail(sender.getAddressDetail());
        entity.setSenderName(sender.getName());
        entity.setSenderAddressStreet(sender.getAddressStreet());
        entity.setSenderPhone(sender.getTelephone());
        entity.setSenderAddressDistrict(sender.getAddressDistrict());
        entity.setSenderAddressCity(sender.getAddressCity());
        entity.setSenderAddressProvince(sender.getAddressProvince());
        entity.setSenderAddressRoomNo(sender.getAddressRoomNo());

        entity.setSenderLatitude(sender.getLatitude());
        entity.setSenderLongitude(sender.getLongitude());
        return entity;
    }

    public static MerchantOrderEntity putSenderFromMerchantInfoEntity(MerchantOrderEntity entity,MerchantInfoEntity sender){
        if (null == entity){
            entity = new MerchantOrderEntity();
        }
        entity.setSenderAddressDetail(sender.getAddressDetail());
        entity.setSenderName(sender.getMerchantName());
        entity.setSenderAddressStreet(sender.getAddressStreet());
        entity.setSenderPhone(sender.getPhone());
        entity.setSenderAddressDistrict(sender.getAddressDistrict());
        entity.setSenderAddressCity(sender.getAddressCity());
        entity.setSenderAddressProvince(sender.getAddressProvince());
        entity.setSenderAddressRoomNo(sender.getAddressRoomNo());

        entity.setSenderLatitude(sender.getLatitude());
        entity.setSenderLongitude(sender.getLongitude());
        return entity;
    }
    public static MerchantOrderEntity putReceiverAddressInfo(MerchantOrderEntity entity,AddressInfo receiver){
        if (null == entity){
            entity = new MerchantOrderEntity();
        }
        entity.setReceiverAddressDetail(receiver.getAddressDetail());
        entity.setReceiverName(receiver.getName());
        entity.setReceiverAddressStreet(receiver.getAddressStreet());
        entity.setReceiverPhone(receiver.getTelephone());
        entity.setReceiverAddressDistrict(receiver.getAddressDistrict());
        entity.setReceiverAddressCity(receiver.getAddressCity());
        entity.setReceiverAddressProvince(receiver.getAddressProvince());
        entity.setReceiverAddressRoomNo(receiver.getAddressRoomNo());

        entity.setReceiverLatitude(receiver.getLatitude());
        entity.setReceiverLongitude(receiver.getLongitude());
        return entity;
    }
}
