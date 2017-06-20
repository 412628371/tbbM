package com.xinguang.tubobo.merchant.web.common;

import com.xinguang.tubobo.impl.merchant.common.ConvertUtil;
import com.xinguang.tubobo.impl.merchant.entity.MerchantInfoEntity;
import com.xinguang.tubobo.impl.merchant.entity.MerchantOrderEntity;
import com.xinguang.tubobo.merchant.web.common.info.AddressInfo;

/**
 * 将地址信息设置到实体的工具类.
 */
public class AddressInfoToOrderBeanHelper {
    public static MerchantOrderEntity putSenderFromAddressInfo(MerchantOrderEntity entity,AddressInfo sender){
        if (null == entity){
            entity = new MerchantOrderEntity();
        }
        entity.setSenderAddressDetail(ConvertUtil.handleNullString(sender.getAddressDetail()));
        entity.setSenderName(ConvertUtil.handleNullString(sender.getName()));
        entity.setSenderAddressStreet(ConvertUtil.handleNullString(sender.getAddressStreet()));
        entity.setSenderPhone(ConvertUtil.handleNullString(sender.getTelephone()));
        entity.setSenderAddressDistrict(ConvertUtil.handleNullString(sender.getAddressDistrict()));
        entity.setSenderAddressCity(ConvertUtil.handleNullString(sender.getAddressCity()));
        entity.setSenderAddressProvince(ConvertUtil.handleNullString(sender.getAddressProvince()));
        entity.setSenderAddressRoomNo(ConvertUtil.handleNullString(sender.getAddressRoomNo()));

        entity.setSenderLatitude(sender.getLatitude());
        entity.setSenderLongitude(sender.getLongitude());
        return entity;
    }

    public static MerchantOrderEntity putSenderFromMerchantInfoEntity(MerchantOrderEntity entity,MerchantInfoEntity sender){
        if (null == entity){
            entity = new MerchantOrderEntity();
        }
        entity.setSenderAddressDetail(ConvertUtil.handleNullString(sender.getAddressDetail()));
        entity.setSenderName(ConvertUtil.handleNullString(sender.getMerchantName()));
        entity.setSenderAddressStreet(ConvertUtil.handleNullString(sender.getAddressStreet()));
        entity.setSenderPhone(ConvertUtil.handleNullString(sender.getPhone()));
        entity.setSenderAddressDistrict(ConvertUtil.handleNullString(sender.getAddressDistrict()));
        entity.setSenderAddressCity(ConvertUtil.handleNullString(sender.getAddressCity()));
        entity.setSenderAddressProvince(ConvertUtil.handleNullString(sender.getAddressProvince()));
        entity.setSenderAddressRoomNo(ConvertUtil.handleNullString(sender.getAddressRoomNo()));

        entity.setSenderLatitude(sender.getLatitude());
        entity.setSenderLongitude(sender.getLongitude());
        return entity;
    }
    public static MerchantOrderEntity putReceiverAddressInfo(MerchantOrderEntity entity,AddressInfo receiver){
        if (null == entity){
            entity = new MerchantOrderEntity();
        }
        entity.setReceiverAddressDetail(ConvertUtil.handleNullString(receiver.getAddressDetail()));
        entity.setReceiverName(ConvertUtil.handleNullString(receiver.getName()));
        entity.setReceiverAddressStreet(ConvertUtil.handleNullString(receiver.getAddressStreet()));
        entity.setReceiverPhone(ConvertUtil.handleNullString(receiver.getTelephone()));
        entity.setReceiverAddressDistrict(ConvertUtil.handleNullString(receiver.getAddressDistrict()));
        entity.setReceiverAddressCity(ConvertUtil.handleNullString(receiver.getAddressCity()));
        entity.setReceiverAddressProvince(ConvertUtil.handleNullString(receiver.getAddressProvince()));
        entity.setReceiverAddressRoomNo(ConvertUtil.handleNullString(receiver.getAddressRoomNo()));

        entity.setReceiverLatitude(receiver.getLatitude());
        entity.setReceiverLongitude(receiver.getLongitude());
        return entity;
    }
}
